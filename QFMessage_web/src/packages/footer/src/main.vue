<template>
	<div class="main-footer">
		<p>
			|
			<template v-for="item in warpper">
				<a v-bind:href="item.href">{{ item.text }}</a> |
			</template>
		</p>
		<p>
			{{ year }} © {{ name }} |
			<a href="http://www.beian.miit.gov.cn/" rel="nofollow">{{ icpBeian }}</a>
			<template v-for="item in items">
				| <a :href="item.href" rel="nofollow">{{item.text}}</a>
			</template>
		</p>
		<div>
			<a target="_blank" href="http://www.beian.gov.cn/portal/registerSystemInfo?recordcode=13030402000150" style="display:inline-block;text-decoration:none;height:20px;line-height:20px;" rel="nofollow">
				<img src="../../../assets/beian.png" style="float:left;" />
				<p style="float:left;height:20px;line-height:20px;margin: 0px 0px 0px 5px; ">{{ beian }}</p>
			</a>
		</div>
	</div>
</template>

<script>
/**
 * 页脚模块
 * warpper最上层导航，Array
 * [{
 *		href//超链接地址
 * 		text//显示内容
 * }]
 * beian公安备案号 String
 * icpBeian ICP备案号 String
 * startYear 网站开设起始年份，默认今年 String
 * name 网站名称 String
 * items 中层导航 Array
 * [{
 *		href//超链接地址
 * 		text//显示内容
 * }]
 */
export default {
	name:"QfFooter",
	data() {
		return{

		}
	},
	methods: {},
	props: {
		warpper:{
			type:Array,
			default:[]
		},
		beian:{
			type:String,
			default:"公网安未备案"
		},
		icpBeian:{
			type:String,
			default:"ICP未备案"
		},
		startYear:{
			type:String,
			default:''
		},
		name:{
			type:String,
			default:''
		},
		items:{
			type:Array,
			default:[]
		}
	},computed:{
		year(){
			var nowYear = new Date().getFullYear();
			if(this.$props.startYear === '')
				return nowYear;
			else if(parseInt(this.$props.startYear) < parseInt(nowYear))
				return this.$props.startYear +"-"+nowYear;
			return nowYear;
		}
	}
};
</script>

<style>
.main-footer {
	width: 100%;
	text-align: center;
	font-size: 14px;
	color: #8c8c8c;
	padding-bottom: 0;
}

.main-footer a:link {
	text-decoration: none;
	color: #8c8c8c;
}

.main-footer a:active {
	text-decoration: none;
	color: #8c8c8c;
}

.main-footer a:hover {
	text-decoration: none;
	color: #8c8c8c;
}

.main-footer a:visited {
	text-decoration: none;
	color: #8c8c8c;
}
</style>
