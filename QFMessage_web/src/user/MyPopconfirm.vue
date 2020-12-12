<template>
	<el-popover placement="top" width="160" style="margin-left: 10px;" trigger="click" v-model="visiable">
		<p>
			<span v-if="$slots.default"><slot></slot></span>
		</p>
		<div style="text-align: right; margin: 0">
			<el-button size="mini" type="success" @click="cancel">取消</el-button>
			<el-button type="danger" size="mini" @click="confirm">确定</el-button>
		</div>
		<el-tooltip slot="reference" :content="content" :placement="placement" :open-delay="500" :enterable="false">
			<el-button :type="type" :icon="icon" :circle="true" :size="size"></el-button>
		</el-tooltip>
	</el-popover>
</template>

<script>
	
export default {
	data() {
		return {
			visiable: false
		};
	},
	methods:{
		confirm(){
			this.visiable = false;
			this.$emit('on-confirm');
		},
		cancel(){
			this.visiable = false;
		}
	},props:{
		content:{
			type:String,
			default:""
		},
		size:{
			type:String,
			default:""
		},
		placement:{
			type:String,
			default:"bottom"
		},
		icon:{
			type:String,
			default:"el-icon-delete"
		},
		type:{
			type:String,
			default:"danger"
		}
	}
};
</script>

<style></style>
