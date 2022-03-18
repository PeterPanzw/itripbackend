package cn.itrip.dao.itripUserLinkUser;
import cn.itrip.pojo.ItripUserLinkUser;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface ItripUserLinkUserMapper {

	public ItripUserLinkUser getItripUserLinkUserById(@Param(value = "id") Long id)throws Exception;

	public List<ItripUserLinkUser>	getItripUserLinkUserListByMap(Map<String, Object> param)throws Exception;

	public Integer getItripUserLinkUserCountByMap(Map<String, Object> param)throws Exception;

	public Integer insertItripUserLinkUser(ItripUserLinkUser itripUserLinkUser)throws Exception;

	public Integer updateItripUserLinkUser(ItripUserLinkUser itripUserLinkUser)throws Exception;

	public Integer deleteItripUserLinkUserById(@Param(value = "id") Long id)throws Exception;

	//查
	public List<ItripUserLinkUser> selectUserLinkUser(@Param(value = "id") Long id,@Param(value = "name")String name)throws Exception;
	//新增
	public int insertUserLinkUser(ItripUserLinkUser i)throws Exception;
	//删除
	public int deleteUserLinkUser(@Param("ids")Integer[] ids)throws Exception;
	//修改
	public int updateUserLinkUser(ItripUserLinkUser itripUserLinkUser)throws Exception;
}
