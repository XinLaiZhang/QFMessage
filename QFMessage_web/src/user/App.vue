<template>
	<div id="wrap">
		<!-- 背景 -->
		<div id="background"><img src="../assets/background.jpg" width="100%" /></div>
		<!-- 主内容部分 -->
		<div class="main-body">
			<qf-header @on-click="headerClick" :src="require('../assets/logo.png')" :items="headerItem" index="../index.html"></qf-header>
			<div class="main-content">
				<div class="content" id="groupTable">
					<!-- 文字导航 -->
					<el-row>
						<el-col style="padding:20px 10px 10px 20px" :span="24" class="title">
							<el-breadcrumb separator-class="el-icon-arrow-right">
								<el-breadcrumb-item :to="{ path: '/' }">全部群</el-breadcrumb-item>
								<el-breadcrumb-item v-for="item in routerMap" :key="item.name" v-bind:to="item.to">{{ item.name }}</el-breadcrumb-item>
							</el-breadcrumb>
						</el-col>
					</el-row>
					<router-view></router-view>
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
import QfGroupList from './QfGroupList.vue';
import VueRouter from 'vue-router';
import QfGroupTask from './QfGroupTask.vue';
import { decode, encode } from './base64Code.js';
var Res = function response() {
	myjquery('#background img').height(myjquery(window).height());
	myjquery('.Lline').height((myjquery('.Lline').width() * 12) / 17 + 'px');
};
myjquery(window).resize(Res);
const router = new VueRouter({
	routes: [
		{
			path: '/',
			name: 'home',
			component: QfGroupList,
			meta: '全部群',
			props: route => ({ id: decode(route.query.id), path: decode(route.query.path), type: decode(route.query.type) })
		},
		{
			path: '/task/',
			name: 'task',
			component: QfGroupTask,
			props: route => ({ id: decode(route.query.id), path: decode(route.query.path), type: decode(route.query.type) })
		}
	]
});

export default {
	data() {
		return {
			qq:"",
			headerItem: [
				{
					href: '#',
					text: '欢迎您，'
				},
				{
					href: '#',
					text: '退出登录'
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
			routerMap: [],
			GroupListMap: new Map()
		};
	},
	methods: {
		headerClick(name){
			if("退出登录" == name){
				this.$http.post('../api/loginOut', {}, { emulateJSON: true }).then(
					data => {
						if (data.body.code == 200 || data.body.code == 302) {
							window.location.href = '../index.html';
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
			}
		}
	},
	components: {
		QfFooter: footer,
		QfHeader: header
	},
	mounted: () => {
		//myjquery('.form').animate({ top: '15%' }, 'slow');
		Res();
	},
	updated: () => {
		Res();
	},
	created(){
		this.$http.post('../api/getAdminQQ', {}, { emulateJSON: true }).then(
			data => {
				if (data.body.code == 200) {
					this.$data.qq = data.body.data;
					this.$data.headerItem[0].text += this.$data.qq;
				}else if(data.body.code == 302 || data.body.code == 303){
					window.location.href = '../login.html';
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
	},
	router
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
	overflow-x: auto;
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
	min-width: 655px;
}
.main-content {
	position: relative;
	width: 100%;
	height: 80%;
}
.content {
	background-color: #fff;
	width: 60%;
	height: 95%;
	margin: auto;
	border-radius: 18px;
	box-shadow: 0px 0px 2px #b2babb;
	min-width: 650px;
}
.el-table__indent {
	float: left;
}
.loading {
	border-radius: 10px 10px 18px 18px;
	height: 80%;
	top: 20%;
}
.borderRadius {
	border-radius: 15px;
}
.el-tabs--border-card > .el-tabs__content {
	padding: 0px;
}
.el-scrollbar__wrap {
	overflow-x: hidden;
}
.form-class{
	border-radius: 15px !important;
}
.el-dialog__wrapper{
	overflow-y: hidden;
}
.type-class{
	min-height: 80px;
}


.textPattern-class{
	border: 1px solid #DCDFE6;
}
.el-card__body{
	padding: 10px;
}
</style>
