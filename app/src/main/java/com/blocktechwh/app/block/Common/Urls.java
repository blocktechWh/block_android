package com.blocktechwh.app.block.Common;

/**
 * Created by eagune on 2017/11/1.
 */
public class Urls {

    /**
     * Host
     */
    //private static  String HOST="http://10.0.0.7/";
    private static  String HOST="http://111.231.146.57:20086/";


    public static final String RegistorActiveCode = HOST+"front/yzm-reg/";

    /**
     * 注册POST
     * phone:String 手机号
     * identifyCode:String 验证码
     * password:String 密码
     * rePassword:String 再次输入密码
     *
     * 返回值:{userInfo:{userImg:String用户头像地址,userName:String用户姓名,userId:Int用户id,userQrCodeString:String用户信息二维码串},token:String}
     */
    public static final String Registor = HOST+"front/reg";

    /**
     * 更改登录POST
     * account:String 手机号
     * password:String 密码
     *
     * 返回值:{userInfo:{userImg:String用户头像地址,userName:String用户姓名,userId:Int用户id,userQrCodeString:String用户信息二维码串},token:String}
     */
    public static final String Login = HOST+"front/login";

    /**
     * 退出登录GET
     */
    public static final String Logout = HOST+"front/logout/";

    /**
     * 更改用户信息PUT
     * userNmae:String
     * sex:Int 1男 2女
     * email:String
     * birthDay:String (yyyy-MM-dd)
     * address:String
     */
    public static final String UpdateUserInfo = HOST+"front/user/update";

    /**
     * 忘记密码验证码POST
     * phone:String
     */
    public static final String ForgetPasswordActiveCode = HOST+"front/forgetpwdcode/";

    /**
     * 忘记密码POST
     * phone:String
     * code:String
     * passWord:String
     */
    public static final String ForgetPassword = HOST+"front/forgetpwd/";

    /**
     * 修改密码POST
     * oldPassWord:String
     * newPassWord:String
     */
    public static final String UpdatePassword = HOST+"front/updatepwd/";

    /**
     * 获取用户联系人列表GET
     *
     * 返回值:[{userImg:String联系人头像地址,userName:String联系人姓名,userId:Int联系人id,id:Int添加记录id},...,{}]
     */
    public static final String Contacts = HOST+"front/link/contacts";

    /**
     * 获取用户好友请求GET
     *
     * 返回值: count:数量
     */
    public static final String ContactRequestsCount = HOST+"front/link/new-count";

    /**
     * 获取用户好友请求列表GET
     *
     * 返回值:[{userImg:String联系人头像地址,userName:String联系人姓名,userId:int联系人id,remark:String留言},...,{}]
     */
    public static final String ContactRequestsList = HOST+"front/getaddcontact/";

    /**
     * 根据用户id获取用户信息GET
     * userId:Int
     *
     * 返回值:{userName:Sring,sex:Int,phone:String,email:String,address:String}
     */
    public static final String GetUserInfoById = HOST+"front/getuserbyid/";

    /**
     * 根据用户信息二维码串获取用户信息GET
     * userQrCodeString:Int
     *
     * 返回值:{userName:Sring,sex:Int,phone:String,email:String,address:String}
     */
    public static final String GetUserInfoByQr = HOST+"front/getuserbyqrcode/";

    /**
     * 同意好友请求POST
     * id:Int 添加记录id
     */
    public static final String AgreeContactRequest = HOST+"front/agreeadd/";

    /**
     * 解除好友关系POST
     * userId:Int 好友id
     */
    public static final String DeleteContact = HOST+"front/removecontact/";

    /**
     * 根据手机号搜索用户GET
     * phone 手机号
     *
     * 返回值:{userImg:String用户头像地址,userName:String用户姓名,userId:Int用户id}
     */
    public static final String SearchContact = HOST+"front/user/phone/";

    /**
     * 申请对方为联系人POST
     * userId:Int 用户id
     */
    public static final String RequestContact = HOST+"front/addcontact/";

    /**
     * 向联系人发红包POST
     * userId:Int 用户id
     * amount:Float 金额（存四位 用两位）
     * remark:String 备注语
     */
    public static final String SendGift = HOST+"front/creategift/";

    /**
     * 收红包POST
     * giftId:Int 红包id
     */
    public static final String RecieveGift = HOST+"front/recievegift/";

    /**
     * 查看红包详情GET
     * giftId:Int 红包id
     *
     * 返回值:{giftInfo:{amount:Int,remark:String,SenderInfo:{userImg:String,userName:String}},recieverList:[{},...]}
     */
    public static final String GiftDetail = HOST+"front/recievegift/";

    /**
     * 收到红包列表GET
     *
     * 返回值:[{id:String红包id,amount:金额,SenderInfo:{userImg:String,userName:String},},...]
     */
    public static final String GiftGetList = HOST+"front/gift/receive-list/";

    /**
     * 发出红包列表GET
     *
     * 返回值:[{id:String红包id,amount:金额,type:Int红包类型},...]
     */
    public static final String GiftSendList = HOST+"front/gift/send-list/";

    /**
     * 未接收的红包列表GET(暂时使用)
     *
     * 返回值:[{id:String红包id,amount:金额,SenderInfo:{userImg:String,userName:String},time:发送的时间},...]
     */
    public static final String GiftWaitRecieveList = HOST+"front/gift/toin/";


}
