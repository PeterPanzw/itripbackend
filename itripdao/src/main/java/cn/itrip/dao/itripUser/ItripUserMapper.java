package cn.itrip.dao.itripUser;
import cn.itrip.pojo.ItripUser;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface ItripUserMapper {

	public ItripUser getItripUserById(@Param(value = "id") Long id)throws Exception;

	public ItripUser	getItripUserListByMap(Map<String, Object> param)throws Exception;

	public Integer getItripUserCountByMap(Map<String, Object> param)throws Exception;

	public Integer insertItripUser(ItripUser itripUser)throws Exception;

	public Integer updateItripUser(ItripUser itripUser)throws Exception;

	public Integer deleteItripUserById(@Param(value = "id") Long id)throws Exception;
	//注册
	public int insertUser(ItripUser itripUser)throws Exception;
	//激活
	public int updateUser(@Param(value = "user")String user)throws Exception;
	//查用户名是否存在
	public ItripUser selectUserName(@Param(value = "name")String name)throws Exception;

}
