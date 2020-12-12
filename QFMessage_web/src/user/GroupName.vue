<template>
	<span class="groupName">
		<template v-if="scope.row.hasChildren">
			<span v-show="!edit">
				<a @click="routerTo(scope.row)">{{ scope.row.groupName }}</a>
				<i class="el-icon-edit" style="cursor: pointer;" @click="showEdit()"></i>
			</span>
			<span v-show="edit">
				<el-input
					ref="input"
					v-model="inputValue"
					size="mini"
					autosize
					type="text"
					@blur="edit = false"
					@keyup.enter.native="changeName(scope.$index, scope.row)"
				></el-input>
			</span>
		</template>
		<template v-else>
			<a @click="routerTo(scope.row)">{{ scope.row.groupName }}</a>
		</template>
		<!-- <a href="">{{ scope.row.groupName }}</a> -->
	</span>
</template>

<script>
import {decode, encode} from './base64Code.js';
export default {
	data() {
		return {
			edit: false,
			inputValue: ''
		};
	},
	methods: {
		showEdit() {
			this.$data.edit = true;
			this.$refs.input.focus();
			this.$data.inputValue = this.$props.scope.row.groupName;
		},
		changeName(index, row) {
			this.$http.post('renameFolder', {"folderId":row.id,"folderName":this.$data.inputValue}, { emulateJSON: true }).then(
				data => {
					if (data.body.code == 200) {
						this.$props.scope.row.groupName = this.$data.inputValue;
					} else {
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
			this.$data.edit = false;
		},
		/**
		 * 路由
		 * @param {Object} row
		 */
		routerTo(row) {
			var currentPath = this.$router.currentRoute.query.path ? this.$router.currentRoute.query.path : '';
			var currentId = this.$router.currentRoute.query.id ? this.$router.currentRoute.query.id : '';
			currentPath = decode(currentPath)
			currentId = decode(currentId)
			var to = "/";
			if (!row.hasChildren) {
				to = to + 'task'
			}
			var path = currentPath + '/' + row.groupName;
			var id = currentId + '/' + row.id;
			this.$router.push({path:to,query:{path:encode(path),id:encode(id),type:null}});
		}
	},
	props: {
		scope: {
			type: Object
		}
	}
};
</script>

<style scoped="scoped">
.groupName {
	float: left;
	margin-left: 10px;
	height: 40px;
	line-height: 40px;
	text-align: center;
}
.groupName a {
	text-decoration: none;
	color: inherit;
	cursor: pointer;
}
</style>
