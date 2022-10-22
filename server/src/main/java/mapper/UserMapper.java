package mapper;

import common.User;

public interface UserMapper {
    User selectByNamePwd(User user);
    User selectByuseName(User user);
    int insertUser(User user);
}
