<template>
	<div id="wrap">
		<!-- 视频背景 -->
		<div id="background"><img src="../assets/background.jpg" width="100%" /></div>
		<!-- 主内容部分 -->
		<div class="main-body">
			<qf-header :src="require('../assets/logo.png')" :items="headerItem"></qf-header>
			<div class="main-content">
				<div class="form">
					<h1>账号登陆</h1>
					<el-form id="form" ref="loginForm" :model="form" status-icon :rules="rules" label-width="auto" size="medium">
						<el-form-item prop="name">
							<el-col :span="2"><i class="el-icon-user-solid"></i></el-col>
							<el-col :span="22">
								<el-input v-model="form.name" placeholder="管理员QQ/账号"><i class="el-icon-share"></i></el-input>
							</el-col>
						</el-form-item>
						<el-form-item prop="password">
							<el-col :span="2"><i class="el-icon-lock"></i></el-col>
							<el-col :span="22"><el-input type="password" v-model="form.password" autocomplete="off" placeholder="密码"></el-input></el-col>
						</el-form-item>
						<el-form-item style="margin-bottom:0"><el-button class="submit" type="primary" @click="onSubmit('loginForm')">登陆</el-button></el-form-item>
						<el-form-item>
							<el-col :span="21">
								<el-checkbox v-model="form.agree">记住我</el-checkbox>
								<el-divider direction="vertical"></el-divider>
								<a href="findpassword.html">忘记密码？</a>
							</el-col>
							<el-col :span="3"><a href="register.html" style="color:green">去注册</a></el-col>
						</el-form-item>
						<el-form-item class="otherlogin">
							<p>其他方式登陆</p>
							<a href="#" @click="()=>{this.$message({type: 'success',message: '接入中尽请期待'});}" title="QQ登陆"><img src="../assets/qq.png" alt="QQ登陆" /></a>
							<a href="#" @click="()=>{this.$message({type: 'success',message: '接入中尽请期待'});}" title="微信登陆"><img src="../assets/wechat.png" alt="微信登陆" /></a>
							<a href="#" @click="()=>{this.$message({type: 'success',message: '接入中尽请期待'});}" title="易班登陆"><img src="../assets/yiban.png" alt="易班登陆" /></a>
						</el-form-item>
					</el-form>
				</div>
			</div>
			<qf-footer beian="冀公网安备 13030402000150号" icpBeian="冀ICP备20003950号" :warpper="warpper" name="小辰同学" start-year="2020" :items="footerItems"></qf-footer>
		</div>
	</div>
</template>

<script>
import footer from '../packages/footer/src/main.vue';
import header from '../packages/header/src/main.vue';
import myjquery from 'jquery';
import jsencrypt from '../jsencrypt.js';
var Res = function response() {
	myjquery('#background img').height(myjquery(window).height());
	myjquery('.Lline').height((myjquery('.Lline').width() * 12) / 17 + 'px');
};
myjquery(window).resize(Res);
export default {
	data() {
		return {
			headerItem: [
				{
					href: 'index.html',
					text: '首页'
				},
				{
					href: 'mailto:XinLaiZhang2018@outlook.com?subject=清风管理工具问题反馈',
					text: '意见建议'
				}
			],
			warpper: [
				{
					href: '#',
					text: '用户服务协议'
				},
				{
					href: '#',
					text: '隐私政策'
				},
				{
					href: '#',
					text: '联系我们'
				}
			],
			footerItems: [
				{
					href: 'https://github.com/XinLaiZhang/',
					text: '逸辰开源项目'
				},
				{
					href: '#',
					text: '地址 : 河北省秦皇岛市'
				}
			],
			form: {
				name: '',
				password: '',
				agree: false
			},
			rules: {
				name: [
					{
						required: true,
						message: '请输入用户名',
						trigger: 'blur'
					}
				],
				password: [
					{
						validator: (rule, value, callback) => {
							if (value === '') {
								callback(new Error('请输入密码'));
							} else {
								// 不能含有#和空格
								if (!(value.length >= 8 && value.length <= 20)) {
									callback(new Error('密码由8至20个字符组成'));
									return;
								}
								if (value.indexOf('#') != -1 || value.indexOf(' ') != -1) {
									callback(new Error('密码不能含有#和空格'));
									return;
								}
								var typecount = 0;
								if (/^.*[0-9]+.*/.test(value)) typecount++; // 如果含有数字
								if (/^.*[A-Z]+.*/.test(value)) typecount++; //如果含有大写字母
								if (/^.*[a-z]+.*/.test(value)) typecount++; // 如果含有小写字母
								if (/^.*[^A-Za-z0-9]+.*/.test(value)) typecount++; // 如果含有特殊字符
								// 至少含有数字、大写、小写、特殊字符中的3种
								if (typecount < 3) {
									callback(new Error('密码至少含有数字、大写、小写、特殊字符中的3种'));
									return;
								}
								callback();
							}
						},
						required: true,
						trigger: 'blur'
					}
				]
			}
		};
	},
	methods: {
		onSubmit(formName) {
			this.$refs[formName].validate(valid => {
				if (valid) {
					var publickey ='MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCSkHovyYJIlkq4SquycoL0/Z9MNTLGGCDD4Q2WXLcU8taacSeS//YFx7MIIkAmFLT4MR7AF8cwqSIDk1xdhBtCfBdm12x1HUIvmRm6PX3yG3hNM+j8y2HymV9ohvxTmEuNOksNZ5wgGEBzkLZz9SxGOUIDYF4VKqKV6Gd1EUqbbQIDAQAB';
					var enPass = jsencrypt.encode(publickey, this.$data.form.password);
					this.$http.post('api/login', {"username":this.$data.form.name,"password":enPass,"remember":this.$data.form.agree}, { emulateJSON: true }).then(
						data => {
							if (data.body.code == 200) {
								window.location.href = 'user/user.html';
							}else if(data.body.code == 303 || data.body.code == 306 ){
								this.$message({
									type: 'warning',
									message: '用户名或密码错误' 
								});
							}else{
								this.$message({
									type: 'error',
									message: '未知错误' 
								});
							}
						},
						data => {
							this.$message({
								type: 'error',
								message: '网络异常'
							});
						}
					);
				} else {
					console.log('error submit!!');
					return false;
				}
			});
		}
	},
	components: {
		QfFooter: footer,
		QfHeader: header,
	},
	mounted: () => {
		myjquery('.form').animate({ top: '15%' }, 'slow');
		Res();
	},
	updated: () => {
		Res();
	}
};
</script>

<style>
* {
	padding: 0; /*清除内边距*/
	margin: 0; /*清除外边距*/
	font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
}
html,
body {
	height: 100%;
}

#wrap {
	position: relative;
	width: 100%;
	height: 100%;
	overflow: hidden;
}

#background {
	padding: 0;
	margin: 0;
}

.main-body {
	position: absolute;
	top: 0;
	width: 100%;
	height: 100%;
}
.main-content {
	position: relative;
	width: 100%;
	height: 80%;
}

.form {
	position: absolute;
	width: 384px;
	margin: 0 auto;
	background: #fafafa;
	border-radius: 18px;
	box-shadow: 1px 1px 10px #b2babb;
	top: 10%;
	right: 20%;
	height: 363.625px;
}
.form h1 {
	font-size: 20px;
	text-align: center;
	margin: 7%;
}
#form {
	margin: auto;
	width: 90%;
}

.rules a:link {
	text-decoration: none;
	color: green;
}

.rules a:active {
	color: blue;
}

.rules a:visited {
	text-decoration: none;
	color: green;
}

.rules a:hover {
	text-decoration: underline;
	color: green;
}

.submit {
	width: 345.594px;
}
.form a {
	text-decoration: none;
	color: #595959;
}
.form p {
	font-size: 2px;
	color: #595959;
}
.form .otherlogin img {
	width: 34px;
	margin-left: 16px;
}
.form .otherlogin p {
	float: left;
	margin-right: 6px;
}
</style>
