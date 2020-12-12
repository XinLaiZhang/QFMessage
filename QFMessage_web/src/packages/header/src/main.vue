<template>
	<div class="main-header">
		<div class="logo">
			<a :href="index"><img :src="src" alt="logo" @load="load()" /></a>
		</div>
		<div class="nav">
			<ul>
				<li class="item" v-for="item in items" :key="item.text">
					<a @click="onClick(item.text)" :href="item.href">{{ item.text }}</a>
				</li>
			</ul>
		</div>
	</div>
</template>

<script>
import myjquery from 'jquery';

var Res = function response() {
	myjquery('.nav').css('line-height', myjquery('.logo').css('height'));
	myjquery('.main-header').css('height', myjquery('.logo').css('height'));
};

myjquery(window).resize(Res);
export default {
	name: 'QfHeader',
	data() {
		return {};
	},
	methods: {
		load() {
			Res();
		},
		onClick(name){
			this.$emit('on-click',name);
		}
	},
	props: {
		src: {
			type: String,
			default: ''
		},
		items: {
			type: Array,
			default: () => {
				[];
			}
		},
		index: {
			type: String,
			default: './index.html'
		}
	}
};
</script>

<style>
.main-header {
	width: 100%;
	/* height: auto; */
	margin-top: 1%;
}
.logo {
	float: left;
	/* 	width: 300px; */
	text-align: center;
	/* line-height: 100px; */
	/* margin-top: 2%; */
	margin-left: 10%;
}
.logo img {
	width: 128px;
}

.nav {
	float: right;
	margin-right: 10%;
	/* margin-top: 2%; */
	/* margin-left: 0%; */
	/* width: 460px; */
	/* height: 100px; */
}
.nav ul {
	list-style-type: none;
}

.item {
	float: left;
	/* line-height: 100px; */
	color: #595959;
	margin-right: 40px;
	cursor: pointer;
}
.item a:link {
	text-decoration: none;
	color: #595959;
}

.item a:active {
	text-decoration: none;
	color: #595959;
}

.item a:hover {
	text-decoration: none;
	color: #595959;
}

.item a:visited {
	text-decoration: none;
	color: #595959;
}
</style>
