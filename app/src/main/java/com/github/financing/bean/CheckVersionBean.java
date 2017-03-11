package com.github.financing.bean;

/********************************************
 * 作者：Administrator
 * 时间：2016/10/30
 * 描述：
 *******************************************/
public class CheckVersionBean {
    private String versionName;// 版本名
    private String versionCode;//版本号
    private String description;//版本描述
    private String downloadUrl;// 下载地址

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
