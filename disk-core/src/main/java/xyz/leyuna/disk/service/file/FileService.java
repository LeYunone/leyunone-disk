package xyz.leyuna.disk.service.file;


import cn.hutool.core.util.ObjectUtil;
import com.alibaba.cloud.commons.lang.StringUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import xyz.leyuna.disk.command.CacheExe;
import xyz.leyuna.disk.command.SliceUploadExe;
import xyz.leyuna.disk.domain.domain.FileInfoE;
import xyz.leyuna.disk.domain.domain.FileMd5E;
import xyz.leyuna.disk.domain.domain.FileUpLogE;
import xyz.leyuna.disk.domain.domain.FileUserE;
import xyz.leyuna.disk.model.DataResponse;
import xyz.leyuna.disk.model.co.FileInfoCO;
import xyz.leyuna.disk.model.co.FileMd5CO;
import xyz.leyuna.disk.model.co.FileUpLogCO;
import xyz.leyuna.disk.model.co.FileUserCO;
import xyz.leyuna.disk.model.dto.file.DownloadFileDTO;
import xyz.leyuna.disk.model.dto.file.FileDTO;
import xyz.leyuna.disk.model.dto.file.UpFileDTO;
import xyz.leyuna.disk.model.enums.ErrorEnum;
import xyz.leyuna.disk.util.AssertUtil;
import xyz.leyuna.disk.util.FileUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author LeYuna
 * @email 365627310@qq.com
 * @create 2021-12-24 16:55
 * 文件相关服务 [非查询]
 */
@Service
@Log4j2
public class FileService {

    @Autowired
    private CacheExe cacheExe;

    //默认最大520000
    @Value("${disk.max.memory:520000}")
    private Long maxMemory;

    //默认大小5000
    @Value("${disk.max.file:5000}")
    private Long maxFile;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SliceUploadExe sliceUploadExe;

    @Autowired(required = false)
    private HttpServletResponse response;

    /**
     * 分片上传
     *
     * @param upFileDTO
     * @return
     */
    private DataResponse sliceUploadFile(UpFileDTO upFileDTO) {
        return sliceUploadExe.sliceUpload(upFileDTO);
    }

    public DataResponse deleteTempFile(String tempPath) {
        sliceUploadExe.deleteSliceTemp(tempPath);
        return DataResponse.buildSuccess();
    }

    /**
     * 上传文件
     *
     * @param upFileDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public DataResponse savaFile(UpFileDTO upFileDTO) {
        MultipartFile file = upFileDTO.getFile();
        if (ObjectUtil.isEmpty(file)) {
            //没有文件 新增文件夹
            return this.newFolder(upFileDTO);
        }
        //有文件 进行分片上传
        return this.sliceUploadFile(upFileDTO);
    }

    /**
     * 新建文件夹
     *
     * @param upFileDTO
     * @return
     */
    private DataResponse newFolder(UpFileDTO upFileDTO) {
        //记录文件夹信息
        String save = FileInfoE.queryInstance().setName(upFileDTO.getFilename()).save();

        //绑定用户层
        FileUserE.queryInstance().setUserId(upFileDTO.getUserId()).setFileId(save).setFileFolderId(upFileDTO.getFileFolderId()).save();
        return DataResponse.buildSuccess();
    }

    /**
     * 删除文件
     *
     * @param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public DataResponse deleteFile(FileDTO fileDTO) {

        String fileId = fileDTO.getId();
        String userId = fileDTO.getUserId();
        //检查本文件是否属于操作用户
        FileUserCO fileUserCO = FileUserE.queryInstance().setFileId(fileId).setUserId(userId).selectOne();
        AssertUtil.isFalse(ObjectUtil.isEmpty(fileUserCO), ErrorEnum.USER_INFO_ERROR.getName());

        //检查用户上传目录是否存在/正常
        FileUpLogCO fileUpLogCO = FileUpLogE.queryInstance().setUserId(userId).selectOne();
        AssertUtil.isFalse(ObjectUtil.isEmpty(fileUpLogCO), ErrorEnum.SELECT_NOT_FOUND.getName());

        //逻辑删除数据条
        FileUserE.queryInstance().setId(fileUserCO.getId()).setDeleted(1).update();

        //如果这个文件是文件夹 则需要删除其下的所有文件 逻辑与物理
        List<FileUserCO> fileFolders = FileUserE.queryInstance().setUserId(userId).setFileFolderId(fileId).selectByCon();
        Long deleteSize = 0L;
        if (CollectionUtils.isNotEmpty(fileFolders)) {
            //说明是文件夹
            FileInfoE.queryInstance().setId(fileId).setDeleted(1).update();
            for (FileUserCO cFile : fileFolders) {
                deleteSize += signDeleteFile(cFile.getFileId());
            }
            //可能  逻辑删除该文件夹下的所有文件
            FileUserE.deleteFolderCFile(userId, fileId);
        } else {
            //单个文件
            deleteSize = signDeleteFile(fileId);
        }

        //计算用户的新内存总值
        Long newSize = fileUpLogCO.getUpFileTotalSize() - deleteSize;
        FileUpLogE.queryInstance().setId(fileUpLogCO.getId()).setUpFileTotalSize(newSize).update();
        //删除完成
        return DataResponse.buildSuccess();
    }

    /**
     * 删除文件额外操作 返回涉及删除的文件大小
     *
     * @param fileId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Long signDeleteFile(String fileId) {
        //物理删除文件:先检查这个文件是否除了自己这一条 所有用户都不可用了
        List<FileUserCO> fileUserCOS = FileUserE.queryInstance().setFileId(fileId).selectByCon();
        FileInfoCO fileInfoCO = FileInfoE.queryInstance().setId(fileId).selectById();
        if (fileUserCOS.size() == 1) {
            //删除本文件
            FileUtil.deleteFile(fileInfoCO.getFilePath());
            //逻辑删除文件
            FileInfoE.queryInstance().setId(fileInfoCO.getId()).setDeleted(1).update();
            FileMd5CO fileMd5CO = FileMd5E.queryInstance().setFileId(fileInfoCO.getId()).selectOne();
            AssertUtil.isFalse(ObjectUtil.isEmpty(fileMd5CO), ErrorEnum.FILE_UPLOAD_FILE.getName());
            FileMd5E.queryInstance().setId(fileMd5CO.getId()).setDeleted(1).update();
        }
        return fileInfoCO.getFileSize();
    }

    public void downloadFile(DownloadFileDTO fileDTO) {
        ddDownload(fileDTO);
        //        ptDownload(fileDTO);
    }

    /**
     * 多线程快速下载
     *
     * @param fileDTO
     */
    private void threadDownload(DownloadFileDTO fileDTO) {
        FileInfoCO file = getFile(fileDTO.getFileId(), fileDTO.getUserId());

        String fileName = null;
        Long fileSize = file.getFileSize();
        try {
            fileName = URLEncoder.encode(file.getName(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        //设置下载请求头
        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

        Long start = 0L;
        Long end = fileSize;
        OutputStream os = response.getOutputStream();
        //开启多线程下载
        Long threadSize = fileSize / FileService.downloadThreadCount;
        int threadCount = 0;
        while (threadCount <= FileService.downloadThreadCount) {
            start = threadSize * threadCount;
            end = (threadCount + 1) * threadSize;
            threadCount++;
            new Thread().start();
        }
    }

    class DownloadThread implements Runnable {

        private Long start;
        private Long end;
        private OutputStream os;

        public DownloadThread(Long start,Long end,OutputStream os){

        }

        @Override
        public void run() {

        }
    }

    private static int downloadThreadCount = 5;

    /**
     * 断点下载
     *
     * @param fileDTO
     */
    private void ddDownload(DownloadFileDTO fileDTO) {
        OutputStream os = null;
        InputStream is = null;
        FileInfoCO file = getFile(fileDTO.getFileId(), fileDTO.getUserId());
        try {
            Long fileSize = file.getFileSize();
            String fileName = URLEncoder.encode(file.getName(), "UTF-8");
            //设置请求头
            response.setContentType("application/x-download");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setHeader("Accept-Range", "bytes");
            response.setHeader("FileName", fileName);
            response.setHeader("FileSize", String.valueOf(fileSize));
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);

            String range = request.getHeader("Range");
            Long start = 0L;
            Long end = fileSize;
            if (StringUtils.isNotBlank(range)) {
                range = range.replace("bytes=", "").trim();
                String[] split = range.split("-");
                if (split.length == 2) {
                    start = Long.valueOf(split[0]);
                    end = Long.valueOf(split[1]);
                    if (end > fileSize) {
                        end = fileSize;
                    }
                } else {
                    start = Long.valueOf(range.replace("-", "").trim());
                }
            }

            //下载
            Long currentDownLen = end - start;
            String contentRange = new StringBuffer("bytes=").append(start).append("-").append(end).append("/").append(fileSize).toString();
            response.setHeader("Content-Range", contentRange);
            response.setHeader("Content-Length", String.valueOf(currentDownLen));

            os = response.getOutputStream();
            is = new BufferedInputStream(new FileInputStream(file.getFile()));

            is.skip(start);
            byte[] bf = new byte[1024];
            int len = 0;
            int sum = 0;
            while (sum < currentDownLen) {
                int temp = (currentDownLen - sum) > bf.length ? bf.length : (int) (currentDownLen - sum);
                len = is.read(bf, 0, temp);
                sum += len;
                os.write(bf);
            }

        } catch (Exception e) {

        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.flush();
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 普通下载
     *
     * @param fileDTO
     */
    private void ptDownload(DownloadFileDTO fileDTO) {
        FileInfoCO file = this.getFile(fileDTO.getFileId(), fileDTO.getUserId());
        //文件转换成数组byte
        byte[] bytes = FileUtil.FiletoByte(file.getFile());

        byte[] buffer = new byte[1024];
        BufferedInputStream bis = null;
        //输出流
        OutputStream os = null;
        try {
            //设置返回文件信息
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
            response.setContentType("application/octet-stream");
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            os = response.getOutputStream();
            bis = new BufferedInputStream(new ByteArrayInputStream(bytes));
            //写入文件
            while (bis.read(buffer) != -1) {
                os.write(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关流了
            try {
                if (bis != null) {
                    bis.close();
                }
                if (os != null) {
                    os.flush();
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取文件
     *
     * @param fileId 文件id
     * @return
     */
    private FileInfoCO getFile(String fileId, String userId) {

        //检查本文件是否属于操作用户
        FileUserCO fileUserCO = FileUserE.queryInstance().setFileId(fileId).setUserId(userId).selectOne();
        AssertUtil.isFalse(ObjectUtil.isEmpty(fileUserCO), ErrorEnum.USER_INFO_ERROR.getName());

        //获取文件数据
        FileInfoCO fileInfoCO = FileInfoE.queryInstance().setId(fileId).selectById();
        AssertUtil.isFalse(ObjectUtil.isEmpty(fileInfoCO), ErrorEnum.SELECT_NOT_FOUND.getName());

        File file = FileUtil.getFile(fileInfoCO.getFilePath());
        AssertUtil.isFalse(ObjectUtil.isEmpty(file), ErrorEnum.SELECT_NOT_FOUND.getName());
        fileInfoCO.setFile(file);
        return fileInfoCO;
    }

    /**
     * 获取文件 断点下载
     */
    private void continueDownload() {

    }
}
