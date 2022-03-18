package cn.itrip.pojo;



/**
 * Created by donghai on 2017-06-08.
 */
/*@ApiModel(value = "ItripSearchUserLinkUserVO",description = "查询常用联系人")*/
public class ItripSearchUserLinkUserVO {
    /*@ApiModelProperty("[必填] 常用刚联系人姓名")*/
    private String linkUserName;

    public String getLinkUserName() {
        return linkUserName;
    }

    public void setLinkUserName(String linkUserName) {
        this.linkUserName = linkUserName;
    }
}
