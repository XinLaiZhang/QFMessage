<template>
	<div id="wrap">
		<!-- 视频背景 -->
		<div id="background"><img src="../assets/background.jpg" width="100%" /></div>
		<!-- 主内容部分 -->
		<div class="main-body">
			<qf-header :src="require('../assets/logo.png')" :items="headerItem"></qf-header>
			<div class="main-content">
				<div class="form">
					<h1>账号注册</h1>
					<el-form id="form" ref="registerForm" :model="form" status-icon :rules="rules" label-width="auto" size="medium">
						<el-form-item style="margin-bottom:10px" label="管理QQ" prop="qq"><el-input v-model="form.qq" placeholder="请输入管理员QQ号"></el-input></el-form-item>
						<el-form-item style="line-height: 0px;margin-bottom:0px">
							<span class="el-form-item__error" style="color: #909399; position: relative;">请使用管理员QQ，添加机器人QQ:{{robotQQ}}</span>
						</el-form-item>
						
						<el-form-item label="密码" prop="password">
							<el-input type="password" v-model="form.password" autocomplete="off" placeholder="密码请设置8-20个字符"></el-input>
						</el-form-item>
						<el-form-item label="确认密码" prop="checkPass">
							<el-input type="password" v-model="form.checkPass" autocomplete="off" placeholder="请再次输入密码"></el-input>
						</el-form-item>
						<el-form-item label="验证码" prop="vCode">
							<el-input v-model="form.vCode" placeholder="请输入机器人QQ发送的验证码">
								<el-button @click="sendvCode(form.qq)" slot="append" :disabled="vCodeDisable">{{vCodeStr}}</el-button>
							</el-input>
						</el-form-item>
						<el-form-item prop="agree">
							<el-checkbox class="rules" v-model="form.agree">
								我已阅读并同意
								<a href="#">用户服务规则</a>
							</el-checkbox>
						</el-form-item>
						<el-form-item>
							<el-button type="primary" @click="onSubmit('registerForm')">立即注册</el-button>
							<el-button @click="resetForm('registerForm')">重置</el-button>
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
			vCodeStr:"获取验证码",
			vCodeDisable:false,
			robotQQ:"",
			headerItem: [
				{
					href: 'login.html',
					text: '登陆'
				},
				{
					href: 'mailto:XinLaiZhang2018@outlook.com?subject=小辰同学账号注册问题反馈',
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
				qq: '',
				password: '',
				checkPass: '',
				agree: false,
				vCode: ''
			},
			rules: {
				qq: [
					{
						required: true,
						message: '请输入管理员QQ',
						trigger: 'blur'
					},
					{
						min: 3,
						max: 16,
						message: '长度在 3 到 16 个字符',
						trigger: 'blur'
					},
					{
						validator: (rule, value, callback) => {
							var matcher = /^[0-9]+$/;
							if (matcher.test(value)) {
								callback();
							} else {
								callback(new Error('请输入纯数字组成的qq号'));
							}
						},
						required: true,
						trigger: 'blur'
					}
				],
				password: [
					{
						validator: (rule, value, callback) => {
							if (value === '') {
								callback(new Error('请输入密码'));
							} else {
								if (!(value.length >= 8 && value.length <= 20)) {
									callback(new Error('密码由8至20个字符组成'));
									return;
								}
								// 不能含有#和空格
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
								if (this.form.checkPass !== '') {
									this.$refs.registerForm.validateField('checkPass');
								}
								callback();
							}
						},
						required: true,
						trigger: 'blur'
					}
				],
				checkPass: [
					{
						validator: (rule, value, callback) => {
							if (value === '') {
								callback(new Error('请再次输入密码'));
							} else if (value !== this.form.password) {
								callback(new Error('两次输入密码不一致!'));
							} else {
								callback();
							}
						},
						required: true,
						trigger: 'blur'
					}
				],
				agree: [
					{
						validator: (rule, value, callback) => {
							if (value === false) {
								callback(new Error('请阅读并同意用户服务规则'));
							} else {
								callback();
							}
						},
						required: true,
						trigger: 'change'
					}
				],
				vCode: [
					{
						required: true,
						message: '请输入验证码',
						trigger: 'blur'
					},
					{
						validator: (rule, value, callback) => {
							var matcher = /^[0-9]+$/;
							if (matcher.test(value)) {
								callback();
							} else {
								callback(new Error('请输入纯数字验证码'));
							}
						},
						required: true,
						trigger: 'blur'
					}
				]
			},
		};
	},
	methods: {
		onSubmit(formName) {
			this.$refs[formName].validate(valid => {
				if (valid) {
					var publickey =
						'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCSkHovyYJIlkq4SquycoL0/Z9MNTLGGCDD4Q2WXLcU8taacSeS//YFx7MIIkAmFLT4MR7AF8cwqSIDk1xdhBtCfBdm12x1HUIvmRm6PX3yG3hNM+j8y2HymV9ohvxTmEuNOksNZ5wgGEBzkLZz9SxGOUIDYF4VKqKV6Gd1EUqbbQIDAQAB';
					var enPass = jsencrypt.encode(publickey, this.$data.form.password);
					this.$http.post('api/register', {"qq":this.$data.form.qq,"password":enPass,"vCode":this.$data.form.vCode}, { emulateJSON: true }).then(
						data => {
							if (data.body.code == 200) {
								this.$message({
									type: 'success',
									message: '账号注册成功' 
								});
								window.location.href = 'user/user.html';
							}else if(data.body.code == 301){
								this.$message({
									type: 'warning',
									message: '用户已经被注册' 
								});
							}else if(data.body.code == 304){
								this.$message({
									type: 'warning',
									message: '验证码已经过期，请重新获取' 
								});
								this.$data.form.vCode = "";
							}else if(data.body.code == 305){
								this.$message({
									type: 'warning',
									message: '验证码错误，请重试' 
								});
								this.$data.form.vCode = "";
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
		},
		resetForm(formName) {
			this.$refs[formName].resetFields();
		},
		sendvCode(qq) {
			if(/^[0-9]+$/.test(qq)){
				var times = 60;
				this.$data.vCodeDisable = true;
				this.$data.vCodeStr = times +  "秒后重新发送"
				times--;
				//获取验证码
				this.$http.post('api/getBindCode', {"qq":qq}, { emulateJSON: true }).then(
					data => {
						if (data.body.code == 200) {
							this.$message({
								type: 'success',
								message: '验证码已发送至'+this.$data.form.qq + '请注意查收' 
							});
						}else if(data.body.code == 300){
							this.$message({
								type: 'warning',
								message: '请添加机器人QQ：' + this.$data.robotQQ +'后再试' 
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
				
				
				var timeout = window.setInterval(()=>{
					this.$data.vCodeStr = times +  "秒后重新发送"
					times--;
				},1000);
				window.setTimeout(()=>{
					window.clearTimeout(timeout);
					this.$data.vCodeDisable = false;
					this.$data.vCodeStr = "获取验证码";
				},60000);
			}
		}
	},
	components: {
		QfFooter: footer,
		QfHeader: header
	},
	mounted: () => {
		myjquery('.form').animate({ top: '15%' }, 'slow');
		Res();
	},
	updated: () => {
		Res();
	},created() {
		//获取机器人qq
		this.$http.post('api/getRobot', {}, { emulateJSON: true }).then(
			data => {
				if (data.body.code == 200) {
					this.$data.robotQQ = data.body.qq;
				}else {
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
	height: 445px;
	margin: 0 auto;
	background: #fafafa;
	border-radius: 18px;
	box-shadow: 1px 1px 10px #b2babb;
	top: 10%;
	right: 20%;
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
</style>
