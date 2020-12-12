<template>
	<!-- 滚动条 -->
	<el-scrollbar style="height: 90%;">
		<el-table
			:data="tableData.filter(data => !search || data.groupName.toLowerCase().includes(search.toLowerCase()) || data.groupId.includes(search.trim()))"
			style="width: 100%"
			:border="true"
			row-key="id"
			ref="table"
		>
			<el-table-column>
				<!-- 工具表头 -->
				<template slot="header" slot-scope="scope">
					<el-row type="flex" justify="space-between">
						<el-col :xl="{ span: 16 }" :span="15">
							<el-input id="searchControl" v-model="search" placeholder="输入关键字搜索" prefix-icon="el-icon-search"></el-input>
						</el-col>
						<el-col :xl="{ span: 6 }" :span="9" style="width: auto;">
							<el-tooltip content="添加分组" placement="bottom" :open-delay="500" :enterable="false">
								<el-button type="warning" icon="el-icon-folder-add" :circle="true" @click="addFolder(scope.column)"></el-button>
							</el-tooltip>
							<!-- <el-tooltip content="任务列表" placement="bottom" :open-delay="500" :enterable="false">
								<el-button type="primary" icon="el-icon-s-claim" :circle="true" @click="taskList(null, null)"></el-button>
							</el-tooltip> -->
							<el-tooltip content="绑定群" placement="bottom" :open-delay="500" :enterable="false">
								<el-button type="success" icon="el-icon-plus" :circle="true" @click="addGroupDialogForm = true"></el-button>
							</el-tooltip>
							<el-popover v-model="allOpenSetGroup" placement="bottom" width="20px" trigger="click" style="margin-left: 10px;">
								<div style="text-align: center;">
									<span class="el-switch__label el-switch__label--left"><span>命令</span></span>
									<el-switch
										v-model="command"
										active-color="#13ce66"
										inactive-color="#ff4949"
										@change="setGroup(command, null, null, 'command')"
									></el-switch>
									<div class="el-icon-s-tools" style="float: right;margin-top: 2px;cursor: pointer;" @click="openCommandSetting(null, null)"></div>
									<div style="margin:10px 0px 10px 0px"></div>
									<span class="el-switch__label el-switch__label--left"><span>消息审核</span></span>
									<el-switch
										v-model="checkMsg"
										active-color="#13ce66"
										inactive-color="#ff4949"
										@change="setGroup(checkMsg, null, null, 'checkMsg')"
									></el-switch>
								</div>
								<el-tooltip slot="reference" content="批量设置" placement="bottom" :open-delay="500" :enterable="false">
									<el-button type="info" icon="el-icon-setting" :circle="true" @click="openSetGroup(null, null)"></el-button>
								</el-tooltip>
							</el-popover>
							<my-popconfirm @on-confirm="deleteGroup(null, null)" content="批量删除">
								您确定要从列表中
								<font color="#F56C6C">删除</font>
								您选中的群聊吗？
							</my-popconfirm>
						</el-col>
					</el-row>
				</template>
				<!-- 表头 -->
				<!-- 多选框 -->
				<el-table-column type="selection"></el-table-column>
				<!-- id -->
				<!-- <el-table-column label="id" type="index"></el-table-column> -->
				<el-table-column label="群名称" min-width="300">
					<template slot-scope="scope">
						<template v-if="scope.row.hasChildren">
							<el-avatar icon="el-icon-folder" style="float: left;background-color: #E6A23C;"></el-avatar>
						</template>
						<template v-else>
							<el-avatar :src="'https://p.qlogo.cn/gh/' + scope.row.groupId + '/' + scope.row.groupId + '/40'" @error="() => true" style="float: left;">
								<img src="../assets/emply.png" />
							</el-avatar>
						</template>
						<qf-group-name :scope="scope"></qf-group-name>
					</template>
				</el-table-column>
				<el-table-column label="群号" min-width="100">
					<template slot-scope="scope">
						<span>{{ scope.row.groupId }}</span>
					</template>
				</el-table-column>
				<el-table-column align="right" min-width="300" label="操作">
					<template slot-scope="scope">
						<el-tooltip v-if="!scope.row.hasChildren" content="任务列表" placement="bottom" :open-delay="500" :enterable="false">
							<el-button type="primary" icon="el-icon-s-claim" :circle="true" @click="taskList(scope.$index, scope.row)"></el-button>
						</el-tooltip>
						<el-popover placement="bottom" width="20px" trigger="click" style="margin-left: 10px;">
							<div style="text-align: center;">
								<span class="el-switch__label el-switch__label--left"><span>命令</span></span>
								<el-switch
									v-model="command"
									active-color="#13ce66"
									inactive-color="#ff4949"
									@change="setGroup(command, scope.$index, scope.row, 'command')"
								></el-switch>
								<div class="el-icon-s-tools" style="float: right;margin-top: 2px;cursor: pointer;" @click="openCommandSetting(scope.$index, scope.row)"></div>
								<div style="margin:10px 0px 10px 0px"></div>
								<span class="el-switch__label el-switch__label--left"><span>消息审核</span></span>
								<el-switch
									v-model="checkMsg"
									active-color="#13ce66"
									inactive-color="#ff4949"
									@change="setGroup(checkMsg, scope.$index, scope.row, 'checkMsg')"
								></el-switch>
							</div>
							<el-tooltip v-if="!scope.row.hasChildren" slot="reference" content="设置" placement="bottom" :open-delay="500" :enterable="false">
								<el-button type="info" icon="el-icon-setting" :circle="true" @click="openSetGroup(scope.$index, scope.row)"></el-button>
							</el-tooltip>
						</el-popover>
						<my-popconfirm @on-confirm="deleteGroup(scope.$index, scope.row)" content="删除">
							您确定要从列表中
							<font color="#F56C6C">删除</font>
							您选中的群聊吗？
						</my-popconfirm>
					</template>
				</el-table-column>
			</el-table-column>
		</el-table>
		<!-- 添加qq群 -->
		<el-dialog title="添加QQ群" :visible.sync="addGroupDialogForm" custom-class="borderRadius">
			<el-input placeholder="请输入群号" v-model="groupNum" :autofocus="true"><el-button slot="append" @click="addGroupToLable">添加</el-button></el-input>
			<el-table :data="tmpTableData" border style="width: 100%" empty-text="请添加群" row-key="groupId">
				<el-table-column label="群名称" min-width="300">
					<template slot-scope="scope">
						<el-avatar :src="'https://p.qlogo.cn/gh/' + scope.row.groupId + '/' + scope.row.groupId + '/40'" @error="() => true" style="float: left;">
							<img src="../assets/emply.png" />
						</el-avatar>
						<span class="groupName">
							<a>{{ scope.row.groupName }}</a>
						</span>
					</template>
				</el-table-column>
				<el-table-column label="群号" min-width="100">
					<template slot-scope="scope">
						<span>{{ scope.row.groupId }}</span>
						<el-button type="danger" icon="el-icon-close" circle style="float: right;" size="mini" @click="deleteAddGroup(scope.$index, scope.row)"></el-button>
					</template>
				</el-table-column>
			</el-table>
			<div slot="footer" class="dialog-footer">
				<el-button type="info" @click="addGroupDialogForm = false" round>取消</el-button>
				<el-button type="success" @click="addGroupList()" round>添加</el-button>
			</div>
		</el-dialog>
		<!-- 修改命令设置 -->
		<el-dialog title="修改群设置" :visible.sync="commandSetting.commandDialog" custom-class="borderRadius">
			<el-table :data="commandSetting.tableData" style="width: 100%" :border="true">
				<!-- 表头 -->
				<el-table-column label="命令名" min-width="100">
					<template slot-scope="scope">
						<span>{{ scope.row.commandName }}</span>
					</template>
				</el-table-column>
				<el-table-column label="权限" min-width="400">
					<template slot-scope="scope">
						<el-tag style="margin-right: 2px;" :key="tag" v-for="tag in scope.row.power" closable :disable-transitions="false" @close="deletePower(scope.$index, tag)">{{tag == "0" ? "管理员":tag == "1" ? "群主":tag}}</el-tag>
						<el-input style="width: 90px;margin-left: 10px;vertical-align: bottom;" v-if="scope.row.inputVisible" v-model="scope.row.inputValue" size="small" @keyup.enter.native="addPower(scope.$index, scope.row)" @blur="addPower(scope.$index, scope.row)"></el-input>
						<el-button v-else size="small" @click="scope.row.inputVisible = true">添加权限</el-button>
					</template>
				</el-table-column>
				<el-table-column label="操作">
					<template slot-scope="scope">
						<el-button v-if="!scope.row.open" size="mini" @click="scope.row.open = true">启用</el-button>
						<el-button v-else size="mini" type="danger" @click="scope.row.open = false">停用</el-button>
					</template>
				</el-table-column>
			</el-table>
			<p style="font-size: 12px;text-align: center;">命令权限：0为管理员，1为群主，其余认为为qq号</p>
			<div slot="footer" class="dialog-footer">
				<el-button type="info" @click="commandSetting.commandDialog = false" round>取消</el-button>
				<el-button type="success" @click="changeCommand()" round>保存</el-button>
			</div>
		</el-dialog>
	</el-scrollbar>
</template>

<script>
import myPopconfirm from './MyPopconfirm.vue';
import QfGroupName from './GroupName.vue';
import { decode, encode } from './base64Code.js';
/**
 * 计算面包屑导航路径
 * @param {Object} idList idlist列表
 * @param {Object} pathList pathlist列表
 */
function calcPath(idList, pathList) {
	let routerMap = [];
	if (idList == null || pathList == null || pathList.length != idList.length) return routerMap;
	let id = '';
	let path = '';
	for (let i = 1; i < idList.length; i++) {
		path = path + '/' + pathList[i];
		id = id + '/' + idList[i];
		let to = '/?path=' + encode(path) + '&id=' + encode(id);
		if (i == idList.length - 1) to = '';
		routerMap.push({ name: pathList[i], to: to });
	}
	return routerMap;
}
export default {
	data() {
		return {
			tableData: [],
			search: '', //搜索数据
			command: false, //匿名设置
			checkMsg: false, //禁言设置
			addGroupDialogForm: false, //添加群
			groupNum: '', //群号
			tmpTableData: [], //添加群时数据
			allOpenSetGroup: false, //设置多个群
			commandSetting:{
				commandDialog: false, //设置命令窗口
				commandGroupId: [],   //群命令设置群号
				tableData:[],		  //表格数据{
												// 	commandId:"1",
												// 	commandName:"帮助",
												// 	power:["0","1","986675982"],
												// 	open:true,
												// 	inputVisible: false,
												// 	inputValue: ''
												// }
			}
		};
	},
	props: {
		id: {
			type: String
		},
		path: {
			type: String
		}
	},
	methods: {
		/**
		 * 增加文件夹
		 * @param {Object} column
		 */
		addFolder(column) {
			let selection = this.$refs.table.selection;
			let params = {};
			if (selection.length > 0) {
				let folderId = [];
				let groupId = [];
				for (let i = 0; i < selection.length; i++) {
					if (selection[i].hasChildren) {
						folderId.push(selection[i].id);
					} else {
						groupId.push(selection[i].id);
					}
				}
				if (folderId.length > 0) {
					params['folderId'] = folderId.toString();
				}
				if (groupId.length > 0) {
					params['groupId'] = groupId.toString();
				}
			}
			if (this.$props.id == null) {
				this.$http.post('createFolder', params, { emulateJSON: true }).then(
					data => {
						if (data.body.code == 200) {
							this.$data.tableData.push(data.body.data);
							if (params.folderId != null || params.groupId != null) {
								for(let i = 0;i < selection.length;i++){
									this.$data.tableData.splice(this.$data.tableData.findIndex(item => item.id == selection[i].id), 1);
								}
								// let j = 0;
								// for (let i = 0; i < this.$data.tableData.length; i++) {
								// 	if ((this.$data.tableData[i].id = selection[j].id)) {
								// 		this.$data.tableData.splice(i, 1);
								// 		j++;
								// 	}
								// }
							}
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
			} else {
				let idList = this.$props.id.split('/');
				let id = idList[idList.length - 1];
				params['id'] = id;
				this.$http.post('createFolder', params, { emulateJSON: true }).then(
					data => {
						if (data.body.code == 200) {
							this.$data.tableData.push(data.body.data);
							if (params.folderId != null || params.groupId != null) {
								for(let i = 0;i < selection.length;i++){
									this.$data.tableData.splice(this.$data.tableData.findIndex(item => item.id == selection[i].id), 1);
								}
								// let j = 0;
								// for (let i = 0; i < this.$data.tableData.length; i++) {
								// 	if ((this.$data.tableData[i].id = selection[j].id)) {
								// 		this.$data.tableData.splice(i, 1);
								// 		j++;
								// 	}
								// }
							}
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
			}
		},
		/**
		 * 任务列表
		 * @param {Object} index
		 * @param {Object} row
		 */
		taskList(index, row) {
			let to = '/task';
			let currentPath = this.$router.currentRoute.query.path ? this.$router.currentRoute.query.path : '';
			let currentId = this.$router.currentRoute.query.id ? this.$router.currentRoute.query.id : '';
			currentPath = decode(currentPath);
			currentId = decode(currentId);
			if (index == null) {
				//多项式
				let selection = this.$refs.table.selection;
				if (selection.length > 0) {
					let selectId = '';
					let selectPath = '';
					selection.forEach((item, index) => {
						selectId = selectId + '/' + item.id;
						if (index == 0) selectPath = item.groupName;
						else selectPath = selectPath + '，' + item.groupName;
					});
					let path = currentPath + '/' + selectPath;
					let id = selectId;
					let type = 'list';
					this.$router.push({ path: to, query: { path: encode(path), id: encode(id), type: encode(type) } });
				} else {
					this.$message({
						message: '请选择至少一个群',
						type: 'warning',
						customClass: 'borderRadius',
						center: true
					});
				}
			} else if (row != null && row.hasChildren) {
				let path = currentPath + '/' + row.groupName;
				let id = currentId + '/' + row.id;
				let type = 'folder';
				this.$router.push({ path: to, query: { path: encode(path), id: encode(id), type: encode(type) } });
			} else {
				//单个
				this.$children[0].$children[0].$children[2].$children[3].routerTo(row);
			}
		},
		/**
		 * 删除群从列表内
		 * @param {Object} index
		 * @param {Object} row
		 */
		deleteGroup(index, row) {
			let params = {};
			let folderId = [];
			let groupId = [];
			if (index == null) {
				//多项式
				let selection = this.$refs.table.selection;
				if (selection.length > 0) {
					for (let i = 0; i < selection.length; i++) {
						if (selection[i].hasChildren) {
							folderId.push(selection[i].id);
						} else {
							groupId.push(selection[i].id);
						}
					}
					if (folderId.length > 0) {
						params['folderId'] = folderId.toString();
					}
					if (groupId.length > 0) {
						params['groupId'] = groupId.toString();
					}
				} else {
					this.$message({
						message: '请选择至少一个群',
						type: 'warning',
						customClass: 'borderRadius',
						center: true
					});
				}
			} else {
				//单个
				if (row.hasChildren) {
					folderId.push(row.id);
					params['folderId'] = folderId.toString();
				} else {
					groupId.push(row.id);
					params['groupId'] = groupId.toString();
				}
			}

			this.$http.post('deleteFile', params, { emulateJSON: true }).then(
				data => {
					if (data.body.code == 200) {
						if (params.folderId != null || params.groupId != null) {
							let j = 0;
							let k = 0;
							for(let i = 0;i < folderId.length;i++){
								this.$data.tableData.splice(this.$data.tableData.findIndex(item => item.id == folderId[i]), 1);
							}
							
							for(let i = 0;i < groupId.length;i++){
								this.$data.tableData.splice(this.$data.tableData.findIndex(item => item.id == groupId[i]), 1);
							}
						}
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
		},
		/**
		 * 打开设置页面
		 * @param {Object} index
		 * @param {Object} row
		 */
		openSetGroup(index, row) {
			this.$data.command = false; //匿名设置
			this.$data.checkMsg = false; //禁言设置
			let groupId = [];
			if (index == null) {
				//多项式
				let selection = this.$refs.table.selection;
				if (selection.length > 0) {
					for (let i = 0; i < selection.length; i++) {
						if (!selection[i].hasChildren) {
							groupId.push(selection[i].id);
						}
					}
				} else {
					this.$message({
						message: '请选择至少一个群',
						type: 'warning',
						customClass: 'borderRadius',
						center: true
					});
					setImmediate(() => {
						this.$data.allOpenSetGroup = false;
					}, 20);
					return;
				}
			} else {
				//单个
				if (!row.hasChildren) {
					groupId.push(row.id);
				}
			}
			this.$http.post('getGroupSetting', { groupId: groupId.toString() }, { emulateJSON: true }).then(
				data => {
					if (data.body.code == 200) {
						this.$data.command = data.body.command; //匿名设置
						this.$data.checkMsg = data.body.checkMsg; //禁言设置
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
		},
		/**
		 * 设置群
		 * @param {Object} index
		 * @param {Object} row
		 */
		setGroup(change, index, row, setName) {
			let groupId = [];
			if (index == null) {
				//多项式
				let selection = this.$refs.table.selection;
				if (selection.length > 0) {
					for (let i = 0; i < selection.length; i++) {
						if (!selection[i].hasChildren) {
							groupId.push(selection[i].id);
						}
					}
				}
			} else {
				//单个
				if (!row.hasChildren) {
					groupId.push(row.id);
				}
			}
			let params = {};
			params['groupId'] = groupId.toString();
			params[setName] = change;
			this.$http.post('setGroupSetting', params, { emulateJSON: true }).then(
				data => {
					if (data.body.code == 200) {
						this.$message({
							type: 'primiry',
							message: '设置更新成功'
						});
					} else {
						this.$message({
							type: 'error',
							message: '设置未能生效，未知错误'
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
		/**
		 * 添加进入待加入群列表
		 */
		addGroupToLable() {
			let table = this.$data.tmpTableData;
			let groupNum = this.$data.groupNum;
			if (groupNum === null || groupNum === '') {
				this.$message({
					message: '请输入群号~',
					type: 'warning',
					customClass: 'borderRadius',
					center: true
				});
				return false;
			}
			this.$data.groupNum = '';
			for (let i = 0; i < table.length; i++) {
				if (table[i].groupId === groupNum) {
					this.$message({
						message: '该群已经在待添加列表了哦~',
						type: 'warning',
						customClass: 'borderRadius',
						center: true
					});
					return false;
				}
			}
			this.$http.post('getGroupInfo', { groupId: groupNum }, { emulateJSON: true }).then(
				data => {
					if (data.body.code == 200) {
						table.push({ id: groupNum, groupId: groupNum, groupName: data.body.data, hasChildren: false });
					} else if (data.body.code == 309) {
						//查询失败
						this.$message({
							type: 'error',
							message: '请邀请机器人加入该群聊'
						});
					} else if (data.body.code == 311) {
						//群已经被绑定
						this.$message({
							type: 'error',
							message: '该群已经被绑定'
						});
					} else if (data.body.code == 312) {
						//没有权限
						this.$message({
							type: 'error',
							message: '您在该群没有管理权限'
						});
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
		},
		/**
		 * 删除添加群列表内数据
		 * @param {Object} index 下标
		 * @param {Object} row 行
		 */
		deleteAddGroup(index, row) {
			this.$data.tmpTableData.splice(index, 1);
		},
		/**
		 * 将要添加的群加入列表
		 */
		addGroupList() {
			let groupId = [];
			let params = {};
			for (let i = 0; i < this.$data.tmpTableData.length; i++) {
				groupId.push(this.$data.tmpTableData[i].groupId);
			}
			params['groupId'] = groupId.toString();
			if (this.$props.id != null) {
				let idList = this.$props.id.split('/');
				let id = idList[idList.length - 1];
				params['folderId'] = id;
			}

			//发送ajax请求
			this.$http.post('bindGroup', params, { emulateJSON: true }).then(
				data => {
					if (data.body.code == 200) {
						this.$data.tableData = this.$data.tableData.concat(this.$data.tmpTableData);
						this.$data.tmpTableData = [];
						this.$message({
							message: '添加成功',
							type: 'success',
							customClass: 'borderRadius',
							center: true
						});
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
			this.addGroupDialogForm = false;
		},
		/**
		 * 打开命令界面
		 * @param {Object} index
		 * @param {Object} row
		 */
		openCommandSetting(index, row){
			this.$data.commandSetting.commandGroupId = [];
			this.$data.commandSetting.tableData = [];
			if (index == null) {
				//多项式
				let selection = this.$refs.table.selection;
				if (selection.length > 0) {
					for (let i = 0; i < selection.length; i++) {
						if (!selection[i].hasChildren) {
							this.$data.commandSetting.commandGroupId.push(selection[i].id);
						}
					}
				}
			} else {
				//单个
				if (!row.hasChildren) {
					this.$data.commandSetting.commandGroupId.push(row.id);
				}
			}
			//发送ajax请求
			this.$http.post('getCommand', {"groupId":this.$data.commandSetting.commandGroupId.toString()}, { emulateJSON: true }).then(
				data => {
					if (data.body.code == 200) {
						this.$data.commandSetting.tableData = data.body.data;
						this.$data.commandSetting.commandDialog = true;
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
		},
		/**
		 * 修改命令设置
		 */
		changeCommand(){
			let groupId = this.$data.commandSetting.commandGroupId;
			let command = this.$data.commandSetting.tableData;
			//发送ajax请求
			this.$http.post('updateCommand', {"groupId":groupId.toString(),"commandHttps":JSON.stringify(command)}, { emulateJSON: true }).then(
				data => {
					if (data.body.code == 200) {
						this.$message({
							type: 'success',
							message: '保存成功'
						});
						this.$data.commandSetting.commandDialog = false;
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
			
			
		},
		deletePower(index, tag){
			this.commandSetting.tableData[index].power.splice(this.commandSetting.tableData[index].power.indexOf(tag), 1);
		},
		addPower(index, row){
			let inputValue = row.inputValue;
			if (inputValue) {
				this.commandSetting.tableData[index].power.push(inputValue);
			}
			this.commandSetting.tableData[index].inputVisible = false;
			this.commandSetting.tableData[index].inputValue = '';
		}
	},
	//初始化table
	mounted() {
		let loading = this.$loading({
			target: '#groupTable',
			text: 'Loading',
			background: 'rgba(0, 0, 0, 0.7)',
			customClass: 'loading'
		});
		if (this.$props.id == null) {
			this.$http.post('getFolder', {}, { emulateJSON: true }).then(
				data => {
					if (data.body.code == 200) {
						this.$data.tableData = data.body.data;
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

			//发送默认请求id为空
			// this.$data.tableData = [
			// 	{
			// 		id: 'f1',
			// 		groupName: 'printf("Important Things!\n");',
			// 		groupId: '921697018',
			// 		hasChildren: false
			// 	},
			// 	{
			// 		id: 'a2',
			// 		groupName: '文件夹',
			// 		groupId: '',
			// 		hasChildren: true
			// 	},
			// 	{
			// 		id: 's3',
			// 		groupName: 'fivem 允恭联机 2群',
			// 		groupId: '194675133',
			// 		hasChildren: false
			// 	},
			// 	{
			// 		id: 's4',
			// 		groupName: '人机交互技术-吴恒洋&薛建新',
			// 		groupId: '1065573048',
			// 		hasChildren: false
			// 	},
			// 	{
			// 		id: 'f5',
			// 		groupName: '外包项目',
			// 		groupId: '538331232',
			// 		hasChildren: false
			// 	}
			// ];
			//设置路径为空
			this.$router.app.$data.routerMap = [];
		} else {
			let idList = this.$props.id.split('/');
			let id = idList[idList.length - 1];

			this.$http.post('getFolder', { id: id }, { emulateJSON: true }).then(
				data => {
					if (data.body.code == 200) {
						this.$data.tableData = data.body.data;
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
			//根据id查询
			// this.$data.tableData = [
			// 	{
			// 		id: 'a6',
			// 		groupName: '2020定向越野 参赛人员群',
			// 		groupId: '883956147',
			// 		hasChildren: false
			// 	},
			// 	{
			// 		id: 'b7',
			// 		groupName: '2019-ccpc-wananfly-camp',
			// 		groupId: '548169891',
			// 		hasChildren: false
			// 	}
			// ];
			this.$router.app.$data.routerMap = calcPath(idList, this.$props.path.split('/'));
		}
		loading.close();
	},
	components: {
		MyPopconfirm: myPopconfirm,
		QfGroupName: QfGroupName
	},
	watch: {
		//监控id根据id获取数据
		id: function(newVal, oldVal) {
			let loading = this.$loading({
				target: '#groupTable',
				text: 'Loading',
				background: 'rgba(0, 0, 0, 0.7)',
				customClass: 'loading'
			});
			let app = this.$router.app.$data;
			if (!app.GroupListMap.has(oldVal)) app.GroupListMap.set(oldVal, this.$data.tableData);
			if (app.GroupListMap.has(newVal)) this.$data.tableData = app.GroupListMap.get(newVal);
			else {
				//发送ajax获取数据
				if (newVal == null) {
					//发送默认为空的请求
					this.$http.post('getFolder', {}, { emulateJSON: true }).then(
						data => {
							if (data.body.code == 200) {
								this.$data.tableData = data.body.data;
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
					// this.$data.tableData = [
					// 	{
					// 		id: '1',
					// 		groupName: 'printf("Important Things!\n");',
					// 		groupId: '921697018',
					// 		hasChildren: false
					// 	},
					// 	{
					// 		id: '2',
					// 		groupName: '文件夹',
					// 		groupId: '',
					// 		hasChildren: true
					// 	},
					// 	{
					// 		id: '3',
					// 		groupName: 'fivem 允恭联机 2群',
					// 		groupId: '194675133',
					// 		hasChildren: false
					// 	},
					// 	{
					// 		id: '4',
					// 		groupName: '人机交互技术-吴恒洋&薛建新',
					// 		groupId: '1065573048',
					// 		hasChildren: false
					// 	},
					// 	{
					// 		id: '5',
					// 		groupName: '外包项目',
					// 		groupId: '538331232',
					// 		hasChildren: false
					// 	}
					// ];
				} else {
					let idList = newVal.split('/');
					let id = idList[idList.length - 1];
					this.$http.post('getFolder', { id: id }, { emulateJSON: true }).then(
						data => {
							if (data.body.code == 200) {
								this.$data.tableData = data.body.data;
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
					//根据id发送ajax
					// 	this.$data.tableData = [
					// 		{
					// 			id: '6',
					// 			groupName: '2020定向越野 参赛人员群',
					// 			groupId: '883956147',
					// 			hasChildren: false
					// 		},
					// 		{
					// 			id: '7',
					// 			groupName: '2019-ccpc-wananfly-camp',
					// 			groupId: '548169891',
					// 			hasChildren: false
					// 		}
					// 	];
				}
			}
			loading.close();
		},
		//监控path，控制路径导航
		path: function(newVal, oldVal) {
			let app = this.$router.app.$data;
			if (newVal == null) app.routerMap = [];
			else {
				let idList = this.$props.id.split('/');
				let pathList = newVal.split('/');
				app.routerMap = calcPath(idList, pathList);
			}
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

.dialog-fade-enter-active {
	-webkit-animation: dialog-fade-in 0.5s;
	animation: dialog-fade-in 0.5s;
}
.dialog-fade-leave-active {
	-webkit-animation: dialog-fade-out 0.5s;
	animation: dialog-fade-out 0.5s;
}
@-webkit-keyframes dialog-fade-in {
	0% {
		-webkit-transform: scale(0) translate();
		transform: scale(0);
		opacity: 0;
	}
	100% {
		-webkit-transform: scale(1);
		transform: scale(1);
		opacity: 1;
	}
}
@keyframes dialog-fade-in {
	0% {
		-webkit-transform: scale(0);
		transform: scale(0);
		opacity: 0;
	}
	100% {
		-webkit-transform: scale(1);
		transform: scale(1);
		opacity: 1;
	}
}
@-webkit-keyframes dialog-fade-out {
	0% {
		-webkit-transform: scale(1);
		transform: scale(1);
		opacity: 1;
	}
	100% {
		-webkit-transform: scale(0);
		transform: scale(0);
		opacity: 0;
	}
}
@keyframes dialog-fade-out {
	0% {
		-webkit-transform: scale(1);
		transform: scale(1);
		opacity: 1;
	}
	100% {
		-webkit-transform: scale(0);
		transform: scale(0);
		opacity: 0;
	}
}
</style>
