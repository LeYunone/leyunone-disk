package xyz.leyuna.disk.model.enums;

/**
 * @author LeYuna
 * @email 365627310@qq.com
 * @create 2021-12-28 09:46
 */
public enum  FileEnum {

    FILE_IMG("图片",1,"BMP,JPG,JPEG,PNG,GIF,PSD,PDD,PDF,PCX,DXF,WMF,EMF,LIC,EPS,TGA"),
    FILE_MUSIC("音乐",2,"MP3,MP4,WAV,MID,ASF,MPG,AVI,TTI,CDA,WMA,RA,MIDI,OGG,APE"),
    FILE_VIDEO("视频",3,"AVI,MOV,ASF,RM,NAVI,DIVX,MPEG,RM"),
    FILE_WORD("文档",4,"DOC,TXT,PPTX,PPT,DOCX,XLSX"),
    FILE_OTHER("其他文件",5,"EXE,RAR");

    private String name;
    private Integer value;
    private String fileType;

    FileEnum(String name,Integer value,String fileType){
        this.name=name;
        this.value=value;
        this.fileType=fileType;
    }

    public String getName(){
        return this.name;
    }

    public Integer getValue(){
        return this.value;
    }

    public String getFileType(){
        return this.fileType;
    }

    public static FileEnum loadType(String type){
        FileEnum[] values = values();
        for(FileEnum enums:values){
            if(enums.getFileType().contains(type.toUpperCase())){
                return enums;
            }
        }
        return FileEnum.FILE_OTHER;
    }

    public static FileEnum loadName(Integer value){
        FileEnum[] values = values();
        for (FileEnum enums: values){
            if(enums.getValue() .equals(value)){
                return enums;
            }
        }
        return null;
    }
}
