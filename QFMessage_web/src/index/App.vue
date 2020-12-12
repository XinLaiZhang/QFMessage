<template>
	<div id="wrap">
		<!-- 背景 -->
		<div id="background"><img src="../assets/background.jpg" width="100%" /></div>
		<!-- 主内容部分 -->
		<div class="main-body">
			<qf-header :src="require('../assets/logo.png')" :items="headerItems"></qf-header>
			<div class="main-content">
				<div class="Lline">
					<div class="computer"><img id="showpic" src="../assets/computer.jpg" width="100%" height="100%" /></div>
				</div>
				<div class="Rline">
					<h1>随时随地，轻松管理</h1>
					<p>小辰同学清风&nbsp;专业的群管理工具</p>
					<a href="./user/user.html" class="start">快速开始</a>
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
var Res = function response() {
	myjquery('#background img').height(myjquery(window).height());
	myjquery('.Lline').height((myjquery('.Lline').width() * 12) / 17 + 'px');
};
myjquery(window).resize(Res);

export default {
	data() {
		return {
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
			headerItems: [
				{
					href: 'user/user.html',
					text: '个人中心'
				},
				{
					href: 'mailto:XinLaiZhang2018@outlook.com?subject=清风管理工具问题反馈',
					text: '意见建议'
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
			]
		};
	},
	methods: {},
	components: {
		QfFooter: footer,
		QfHeader: header
	},
	mounted: () => {
		myjquery('.Rline').animate({ top: '40%' }, 'slow');
		myjquery('.Lline').animate({ top: '15%' }, 'slow');
		myjquery('.computer').toggleClass('computer_transform');
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

.Rline {
	position: absolute;
	right: 15%;
	top: 50%;
}
.Rline h1 {
	margin-bottom: 10px;
	/* font-size: 52px; */
	font-size: 2.7vw;
}
.Rline p {
	/* font-size: 19px; */
	font-size: 1vw;
}

.Lline {
	position: absolute;
	/* width: 680px; */
	width: 35.417%;
	/* height: 480px; */
	left: 12%;
	top: 10%;
	perspective: 800px;
}

.computer {
	width: 100%;
	height: 100%;
	margin: 0 auto;
	background: #fafafa;
	border-radius: 18px;
	box-shadow: 1px 1px 10px #b2babb;
	transform: rotateY(0deg);
	-ms-transform: rotateY(0deg); /* IE 9 */
	-moz-transform: rotateY(0deg); /* Firefox */
	-webkit-transform: rotateY(0deg); /* Safari 和 Chrome */
	-o-transform: rotateY(0deg); /* Opera */
}
.computer_transform {
	transform: rotateY(7deg);
	-ms-transform: rotateY(7deg); /* IE 9 */
	-moz-transform: rotateY(7deg); /* Firefox */
	-webkit-transform: rotateY(7deg); /* Safari 和 Chrome */
	-o-transform: rotateY(7deg); /* Opera */
	transition: all 0.5s ease-in-out;
	-moz-transition: all 0.5s ease-in-out; /*Firefox 4 */
	-webkit-transition: all 0.5s ease-in-out; /* Safari和Chrome */
	-o-transition: all 0.5s ease-in-out; /* Opera */
}
.start {
	background-color: #fff;
	border-radius: 8px;
	display: block;
	font-size: 20px;
	width: 142px;
	line-height: 51px;
	text-decoration: none;
	color: inherit;
	text-align: center;
	box-shadow: 0px 0px 5px #b2babb;
	margin-top: 10px;
}
.start:hover {
	opacity: 0.5;
}
</style>
