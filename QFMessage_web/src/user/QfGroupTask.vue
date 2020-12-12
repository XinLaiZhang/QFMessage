<template>
	<!-- 滚动条 -->
	<div style="height: inherit">
		<el-row type="flex" justify="space-between" style="margin:12px 12px">
			<el-col :xl="{ span: 16 }" :span="15"><el-input id="searchControl" v-model="search" placeholder="输入关键字搜索" prefix-icon="el-icon-search"></el-input></el-col>
			<el-col :xl="{ span: 6 }" :span="9" style="width: auto;">
				<tip-button content="新建任务" :open-delay="500" type="success" icon="el-icon-plus" circle @click.native="createTask(null)"></tip-button>
			</el-col>
		</el-row>
		<el-row>
			<el-tabs type="border-card" v-model="selectTab" tab-position="top">
				<el-tab-pane label="全部任务" name="-1"></el-tab-pane>
				<el-tab-pane label="正在进行" name="1"></el-tab-pane>
				<el-tab-pane label="已经完成" name="2"></el-tab-pane>
				<el-tab-pane label="暂未开始" name="0"></el-tab-pane>
			</el-tabs>
		</el-row>
		<el-row style="height: 90%;">
			<el-scrollbar style="height: inherit;">
				<el-card shadow="hover" v-if="filterTask.length == 0"><p style="text-align: center;">暂无数据</p></el-card>
				<transition-group name="el-zoom-in-top" mode="out-in" :appear="true" type="transition" v-else>
					<template v-for="(item, index) in filterTask.filter(data => !search || data.name.toLowerCase().includes(search.toLowerCase()))">
						<el-card shadow="hover" :class="item.status == 1 ? 'success' : item.status == 2 ? 'warning' : 'info'" :key="index">
							<span style="white-space:normal;width: 200px;display: inline-flex;cursor: pointer;" @click="openResult(item)">{{ item.name }}</span>
							<el-date-picker
								readonly
								:type="item.type == 1 ? 'datetime' : 'datetimerange'"
								:value="item.type == 1 ? new Date(item.startTime) : [new Date(item.startTime), new Date(item.endTime)]"
								value-format="yyyy:mm:dd hh:mm:ss"
								class="timePicker"
							></el-date-picker>
							<span style="float: right;">
								<!-- <my-popconfirm v-if="item.status == 1" @on-confirm="stopTask(index, item)" content="停止任务" size="small" placement="top" icon="el-icon-switch-button"> 
								您确定要<font color="#F56C6C">停止</font>您选中的任务吗？
								</my-popconfirm>
								<my-popconfirm v-else-if="item.status == 2" @on-confirm="restartTask(index, item)" content="重置任务" type="primary" size="small" placement="top" icon="el-icon-refresh-right">
									您确定要<font color="#F56C6C">重置</font>您选中的任务吗？
								</my-popconfirm>
								<my-popconfirm v-else @on-confirm="startTask(index, item)" content="开始任务" type="success" size="small" placement="top" icon="el-icon-switch-button">
									您确定要<font color="#F56C6C">开始</font>您选中的任务吗？
								</my-popconfirm> -->
								<tip-button
									content="编辑任务"
									:open-delay="500"
									type="warning"
									size="small"
									icon="el-icon-edit-outline"
									circle
									@click.native="createTask(item)"
									style="margin-left: 10px;"
								></tip-button>
								<my-popconfirm @on-confirm="deleteTask(index, item)" content="删除任务" size="small" placement="top">
									您确定要从列表中
									<font color="#F56C6C">删除</font>
									您选中的任务吗？
								</my-popconfirm>
							</span>
							<div>
								<el-tag :type="item.type == 1 ? '' : item.type == 2 ? 'success' : 'danger'" size="mini">
									{{ item.type == 1 ? '提醒任务' : item.type == 2 ? '统计任务' : '未知任务' }}
								</el-tag>
							</div>
						</el-card>
					</template>
				</transition-group>
			</el-scrollbar>
		</el-row>
		<el-dialog :visible.sync="formPage" width="60%" custom-class="form-class" top="7vh" center="center">
			<div slot="title">任务</div>
			<el-scrollbar style="height: 70vh; width: 100%;">
				<div style="padding-right: 15px;">
					<el-form ref="taskForm" :model="taskForm" label-width="80px" :rules="taskForm.rules" :status-icon="true">
						<el-form-item label="任务名称" prop="name"><el-input v-model="taskForm.name"></el-input></el-form-item>
						<el-form-item label="任务时间" prop="time">
							<el-date-picker v-if="taskForm.type == 1" v-model="taskForm.time" type="datetime" placeholder="提醒时间" align="right"></el-date-picker>
							<el-date-picker
								v-if="taskForm.type == 2"
								v-model="taskForm.time"
								type="datetimerange"
								range-separator="-"
								start-placeholder="开始时间"
								end-placeholder="结束时间"
								:picker-options="taskForm.pickerOptions"
								:unlink-panels="true"
								:default-time="Date.now()"
							></el-date-picker>
						</el-form-item>
						<el-form-item label="任务类型">
							<el-select v-model="taskForm.type" placeholder="请选择" popper-class="type-class">
								<el-option v-for="item in taskForm.typeOption" :key="item.value" :label="item.label" :value="item.value"></el-option>
							</el-select>
						</el-form-item>
						<el-form-item label="提醒消息" prop="msg"><el-input v-model="taskForm.msg" placeholder="提醒时发送的消息"></el-input></el-form-item>
						<el-form-item :label="taskForm.type == 1 ? '提醒方式' : '统计方式'">
							<el-switch v-model="taskForm.isPrivate" active-text="私聊" inactive-text="群聊"></el-switch>
						</el-form-item>
						<span v-show="taskForm.type == 1">
							<el-form-item label="是否重复"><el-switch v-model="taskForm.isRepeat" active-text="是" inactive-text="否"></el-switch></el-form-item>
							<template v-if="taskForm.isRepeat">
								<el-form-item label="重复间隔" prop="repeatTime">
									<el-input v-model="taskForm.repeatTime" placeholder="任务重复的时间间隔,单位为秒."></el-input>
								</el-form-item>
								<el-form-item label="结束时间" prop="endTime">
									<el-date-picker
										v-model="taskForm.endTime"
										type="datetime"
										placeholder="结束时间"
										align="right"
										:picker-options="{
											disabledDate(time) {
												return time.getTime() <= Date.now();
											}
										}"
									></el-date-picker>
								</el-form-item>
							</template>
						</span>
						<span v-show="taskForm.type == 2">
							<el-form-item label="匹配规则" prop="regex">
								<el-col :span="18"><el-input v-model="taskForm.regex" placeholder="请输入正则表达式,分组内数据可以被保存"></el-input></el-col>
								<el-col :span="5" :offset="1">
									<el-popover placement="top" width="400" trigger="click" popper-class="textPattern-class" effect="dark">
										<el-form-item>
											<el-input v-model="myTestPattern.text" placeholder="请输入待匹配文本"><span slot="prepend">待匹配文本</span></el-input>
										</el-form-item>
										<el-form-item>
											<el-input v-model="myTestPattern.pattern" placeholder="请输入正则表达式,需要将待匹配文本全部匹配">
												<span slot="prepend">正则表达式</span>
											</el-input>
										</el-form-item>
										<el-form-item>
											<el-input disabled v-model="myTestPattern.result"><span slot="prepend">匹配结果</span></el-input>
										</el-form-item>
										<el-form-item>
											<span style="padding-left: 10px;">
												<el-tooltip placement="left">
													<el-scrollbar slot="content" style="height: 30vh; ">
														<div class="tipPattern">
															<el-link
																v-for="(item, index) in tipPattern"
																style="font-size: 14px; color: #fff;line-height: 1.7;display: block;"
																@click="myTestPattern.pattern = item.pattern"
																:key="index"
																:underline="false"
															>
																{{ item.title }}：{{ item.pattern }}
															</el-link>
														</div>
													</el-scrollbar>
													<el-link type="primary" style="text-align:start">常用正则表达式</el-link>
												</el-tooltip>
											</span>
											<span style="float: right;">
												<el-button type="success" @click="changeTestPattern">测试匹配</el-button>
												<el-button type="primary" @click="insertPattern">插入结果</el-button>
											</span>
										</el-form-item>
										<el-button type="primary" slot="reference">正则表达式测试</el-button>
									</el-popover>
								</el-col>
							</el-form-item>
							<el-form-item label="消息回复" prop="reply"><el-input v-model="taskForm.reply" placeholder="接收到统计消息后回复的内容."></el-input></el-form-item>
							<el-form-item label="结束提示" prop="endMsg">
								<el-input
									v-model="taskForm.endMsg"
									:placeholder="'结束提示内容,为空则默认为：' + (taskForm.name || '\'任务名称\'') + '结束'"
								></el-input>
							</el-form-item>
							<el-form-item label="结束提醒" prop="remindMsg">
								<el-input v-model="taskForm.remindTime" placeholder="结束前时间,单位分钟"></el-input>
								<div style="margin-top: 10px;"></div>
								<el-input v-model="taskForm.remindMsg" placeholder="结束前的提醒消息"></el-input>
								<div style="margin-top: 10px;"></div>
								<span>提醒方式：</span>
								<el-switch v-model="taskForm.remindIsPrivate" active-text="私聊" inactive-text="群聊"></el-switch>
							</el-form-item>
							<el-form-item label="统计数据">
								<el-transfer v-model="taskForm.saveMsg" :data="saveDateList" :titles="['统计数据', '保存数据']"></el-transfer>
							</el-form-item>
						</span>
						<el-form-item label="名单" v-show="(taskForm.type == 1 && taskForm.isPrivate) || taskForm.type == 2" prop="groupList">
							<el-col :span="14">
								<el-upload
									action="uploadList"
									:drag="true"
									accept=".xlsx"
									:on-success="listSuccess"
									:on-error="listError"
									:on-remove="listRemove"
									auto-upload
									ref="upload"
								>
									<i class="el-icon-upload"></i>
									<div class="el-upload__text">
										将文件拖到此处，或
										<em>点击上传</em>
									</div>
								</el-upload>
							</el-col>
							<el-col :span="10">
								<div>只能上传xls文件，样例</div>
								<el-image :src="require('../assets/example.jpg')" style="height: 139px;"></el-image>
							</el-col>
						</el-form-item>

						<div class="el-form-item" style="text-align: center;">
							<div class="el-form-item__content">
								<el-button>取消</el-button>
								<el-button type="primary" @click="onSubmit">{{ taskForm.dialogButtonName }}</el-button>
							</div>
						</div>
					</el-form>
				</div>
			</el-scrollbar>
		</el-dialog>

		<el-dialog :visible.sync="showTaskResult" width="60%" custom-class="form-class" top="7vh" center="center">
			<span slot="title">{{ taskResult.item.name }}-任务结果</span>
			<el-table :data="taskResult.data" border style="width: 100%" max-height="70vh" size="small">
				<el-table-column prop="qq" label="QQ" width="120"></el-table-column>
				<el-table-column prop="name" label="昵称" width="180"></el-table-column>
				<el-table-column label="任务完成情况" width="100">
					<template slot-scope="scope">
						<span :style="{ color: scope.row.finish ? '#67C23A' : '#F56C6C' }">{{ scope.row.finish ? '完成' : '未完成' }}</span>
					</template>
				</el-table-column>
				<el-table-column v-for="(item, index) in taskResult.format" :key="index" :prop="'data' + index" :label="'保存属性' + index"></el-table-column>
			</el-table>
			<div class="el-form-item" style="text-align: right;margin-top: 10px;">
				<div class="el-form-item__content">
					<el-button size="small" @click="showTaskResult = false">取消</el-button>
					<el-button size="small" type="primary" @click="downLoad">下载名单</el-button>
				</div>
			</div>
		</el-dialog>
	</div>
</template>

<script>
import { decode, encode } from './base64Code.js';
import TipButton from '../packages/button/src/tipButton.vue';
import MyPopconfirm from './MyPopconfirm.vue';
import XLSX from 'xlsx';
function calcPath(idList, pathList, type) {
	var routerMap = [];
	if ((idList == null || pathList == null || pathList.length != idList.length) && (type == null || type == 'folder')) return routerMap;
	var id = '';
	var path = '';
	for (var i = 1; i < pathList.length; i++) {
		path = path + '/' + pathList[i];
		id = id + '/' + idList[i];
		var to = '/?path=' + encode(path) + '&id=' + encode(id);
		if (i == pathList.length - 1) {
			to = '';
			pathList[i] = pathList[i] + '任务列表';
		}
		routerMap.push({ name: pathList[i], to: to });
	}
	return routerMap;
}
/**
 * 通用的打开下载对话框方法，没有测试过具体兼容性
 * @param url 下载地址，也可以是一个blob对象，必选
 * @param saveName 保存文件名，可选
 */
function openDownloadDialog(url, saveName)
{
	if(typeof url == 'object' && url instanceof Blob)
	{
		url = URL.createObjectURL(url); // 创建blob地址
	}
	var aLink = document.createElement('a');
	aLink.href = url;
	aLink.download = saveName || ''; // HTML5新增的属性，指定保存文件名，可以不要后缀，注意，file:///模式下不会生效
	var event;
	if(window.MouseEvent) event = new MouseEvent('click');
	else
	{
		event = document.createEvent('MouseEvents');
		event.initMouseEvent('click', true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
	}
	aLink.dispatchEvent(event);
}

// 将一个sheet转成最终的excel文件的blob对象，然后利用URL.createObjectURL下载
function sheet2blob(sheet, sheetName) {
	sheetName = sheetName || 'sheet1';
	var workbook = {
		SheetNames: [sheetName],
		Sheets: {}
	};
	workbook.Sheets[sheetName] = sheet;
	// 生成excel的配置项
	var wopts = {
		bookType: 'xlsx', // 要生成的文件类型
		bookSST: false, // 是否生成Shared String Table，官方解释是，如果开启生成速度会下降，但在低版本IOS设备上有更好的兼容性
		type: 'binary'
	};
	var wbout = XLSX.write(workbook, wopts);
	var blob = new Blob([s2ab(wbout)], {type:"application/octet-stream"});
	// 字符串转ArrayBuffer
	function s2ab(s) {
		var buf = new ArrayBuffer(s.length);
		var view = new Uint8Array(buf);
		for (var i=0; i!=s.length; ++i) view[i] = s.charCodeAt(i) & 0xFF;
		return buf;
	}
	return blob;
}



export default {
	data() {
		return {
			selectTab: "-1",
			search: '',
			formPage: false,
			showTaskResult: false,
			taskResult: {
				data: [
					// {
					// 	qq:'123456789',
					// 	name:'张逸辰',
					// 	finish:true,
					// 	data0:"张逸辰",
					// 	data1:"96894",
					// 	data2:"2018-20-5",
					// }
				],
				format: [],
				item: {}
			},
			taskForm: {
				m_ID: '', //任务id
				name: '', //任务名称
				isPrivate: false, //任务接受消息是否为私聊
				startTime: '', //开始时间
				endTime: '', //结束时间，重复任务结束时间
				endMsg: '', //结束消息
				msg: '', //提醒消息
				regex: '', //正则表达式
				reply: '', //回复消息
				remindMsg: '', //提醒消息
				remindIsPrivate: false, //提醒是否为私聊
				remindTime: '', //提醒时间
				saveMsg: [], //保存消息
				groupID: '', //群号
				groupList: '', //名单
				isRepeat: false, //提醒任务是否重复
				repeatTime: '', //重复时间
				type: '1', //任务类型
				status: 0, //任务状态
				time: [],
				dialogButtonName: '',
				pickerOptions: {
					shortcuts: [
						{
							text: '最近一周',
							onClick(picker) {
								const start = new Date();
								const end = new Date();
								end.setTime(end.getTime() + 3600 * 1000 * 24 * 7);
								picker.$emit('pick', [start, end]);
							}
						},
						{
							text: '最近一个月',
							onClick(picker) {
								const start = new Date();
								const end = new Date();
								end.setTime(end.getTime() + 3600 * 1000 * 24 * 30);
								picker.$emit('pick', [start, end]);
							}
						},
						{
							text: '最近三个月',
							onClick(picker) {
								const start = new Date();
								const end = new Date();
								end.setTime(end.getTime() - 3600 * 1000 * 24 * 90);
								picker.$emit('pick', [start, end]);
							}
						}
					]
				},
				typeOption: [
					{
						value: '1',
						label: '提醒任务'
					},
					{
						value: '2',
						label: '统计任务'
					}
				],
				rules: {
					name: [
						{
							required: true,
							trigger: 'blur',
							message: '请输入任务名称'
						}
					],
					time: [
						{
							validator: (rule, value, callback) => {
								if (value == null || value == '') {
									callback(new Error('选择任务时间'));
								}
								callback();
							},
							trigger: 'blur'
						}
					],
					msg: [
						{
							required: true,
							trigger: 'blur',
							message: '请输入您要提醒的消息'
						}
					],
					repeatTime: [
						{
							validator: (rule, value, callback) => {
								if (this.$data.taskForm.isRepeat) {
									if (value == '') {
										callback(new Error('请输入重复时间间隔，单位为秒'));
									} else {
										for (let i = 0; i < value.length; i++) {
											if (!(value[i] >= '0' && value[i] <= '9')) {
												callback(new Error('请输入正确的时间间隔'));
												return;
											}
										}
									}
								}
								callback();
							},
							trigger: 'blur'
						}
					],
					endTime: [
						{
							validator: (rule, value, callback) => {
								if (this.$data.taskForm.isRepeat && this.$data.taskForm.type == '1') {
									if (value == '') {
										callback(new Error('请设置结束时间'));
									} else if (new Date(value).getTime() <= new Date(this.$data.taskForm.time).getTime()) {
										callback(new Error('请选择比开始时间长的结束时间'));
									}
								}
								callback();
							},
							trigger: 'blur'
						}
					],
					regex: [
						{
							validator: (rule, value, callback) => {
								if (this.$data.taskForm.type == 2) {
									if (value == null || value == '') {
										callback(new Error('选择输入匹配规则'));
									}
								}
								callback();
							},
							trigger: 'blur'
						}
					],
					reply: [
						{
							validator: (rule, value, callback) => {
								if (this.$data.taskForm.type == 2) {
									if (value == null || value == '') {
										callback(new Error('选择输入收到消息后的回复内容'));
									}
								}
								callback();
							},
							trigger: 'blur'
						}
					],
					remindMsg: [
						{
							validator: (rule, value, callback) => {
								if (this.$data.taskForm.type == 2) {
									if (this.$data.taskForm.remindTime != '') {
										for (let i = 0; i < this.$data.taskForm.remindTime.length; i++) {
											if (!(this.$data.taskForm.remindTime[i] >= '0' && this.$data.taskForm.remindTime[i] <= '9')) {
												callback(new Error('请输入正确的时间'));
												return;
											}
										}
										if (value == null || value == '') {
											callback(new Error('选择输入结束前' + this.$data.taskForm.remindTime + '分钟提醒信息'));
											return;
										}
									}
								}
								callback();
							},
							trigger: 'blur'
						}
					],
					groupList: [
						{
							validator: (rule, value, callback) => {
								if ((this.$data.taskForm.isPrivate && this.$data.taskForm.type == '1') || this.$data.taskForm.type == '2') {
									if (value == '') {
										callback(new Error('请上传提醒名单'));
									}
								}
								callback();
							},
							trigger: 'blur'
						}
					]
				}
			},
			myTestPattern: {
				text: '',
				pattern: '',
				result: ''
			},
			allTask: [
				// {
				// 	m_ID: '1',							//任务id						all
				// 	name: '每日一报',					//任务名称						all
				// 	isPrivate:false,					//任务接受消息是否为私聊			1,接收私聊;2,提醒为私聊
				// 	startTime: '2020-5-17 00:00:00',	//开始时间						all
				// 	endTime: '2020-5-31 00:00:00',		//结束时间，重复任务结束时间		all
				// 	endMsg:'',							//结束消息						1
				// 	msg:'',								//提醒消息						all
				// 	regex:'',							//正则表达式						1
				// 	reply:'',							//回复消息						1
				// 	remindMsg:'',						//提醒消息						1
				// 	remindIsPrivate:false,				//提醒是否为私聊					1
				// 	remindTime:'',						//提醒时间						1
				// 	saveMsg:[1,2],						//保存消息						1
				// 	groupID:"986675982",				//群号							all
				// 	status:"1",							//任务状态						all
				// 	groupList:"",						//名单							all
				// 	isRepeat:false,						//提醒任务是否重复				2
				// 	repeatTime:"300000",				//重复时间						2
				// 	type:'1',							//任务类型
				// }
			],
			filterTaskMap: new Map(),
			tipPattern: [
				{ title: ' 数字', pattern: '^[0-9]*$' },
				{ title: ' n位的数字', pattern: '^\d{n}$' },
				{ title: ' 至少n位的数字', pattern: '^\d{n,}$' },
				{ title: ' m-n位的数字', pattern: '^\d{m,n}$' },
				{ title: ' 零和非零开头的数字', pattern: '^(0|[1-9][0-9]*)$' },
				{ title: ' 非零开头的最多带两位小数的数字', pattern: '^([1-9][0-9]*)+(.[0-9]{1,2})?$' },
				{ title: ' 带1-2位小数的正数或负数', pattern: '^(\-)?\d+(\.\d{1,2})?$' },
				{ title: ' 正数、负数、和小数', pattern: '^(\-|\+)?\d+(\.\d+)?$' },
				{ title: ' 有两位小数的正实数', pattern: '^[0-9]+(.[0-9]{2})?$' },
				{ title: ' 有1~3位小数的正实数', pattern: '^[0-9]+(.[0-9]{1,3})?$' },
				{ title: ' 非零的正整数', pattern: '^[1-9]\d*$' },
				{ title: ' 非零的负整数', pattern: '^-[1-9]\d*$' },
				{ title: ' 非负整数', pattern: '^\d+$' },
				{ title: ' 非正整数', pattern: '^-[1-9]\d*|0$' },
				{ title: ' 非负浮点数', pattern: '^\d+(\.\d+)?$' },
				{ title: ' 非正浮点数', pattern: '^((-\d+(\.\d+)?)|(0+(\.0+)?))$' },
				{ title: ' 正浮点数', pattern: '^[1-9]\d*\.\d*|0\.\d*[1-9]\d*$' },
				{ title: ' 负浮点数', pattern: '^-([1-9]\d*\.\d*|0\.\d*[1-9]\d*)$' },
				{ title: ' 浮点数', pattern: '^(-?\d+)(\.\d+)?$' },
				{ title: ' 汉字', pattern: '^[\u4e00-\u9fa5]{0,}$' },
				{ title: ' 英文和数字', pattern: '^[A-Za-z0-9]+$' },
				{ title: ' 长度为3-20的所有字符', pattern: '^.{3,20}$' },
				{ title: ' 由26个英文字母组成的字符串', pattern: '^[A-Za-z]+$' },
				{ title: ' 由26个大写英文字母组成的字符串', pattern: '^[A-Z]+$' },
				{ title: ' 由26个小写英文字母组成的字符串', pattern: '^[a-z]+$' },
				{ title: ' 由数字和26个英文字母组成的字符串', pattern: '^[A-Za-z0-9]+$' },
				{ title: ' 由数字、26个英文字母或者下划线组成的字符串', pattern: '^\w+$' },
				{ title: ' 中文、英文、数字包括下划线', pattern: '^[\u4E00-\u9FA5A-Za-z0-9_]+$' },
				{ title: ' 中文、英文、数字但不包括下划线等符号', pattern: '^[\u4E00-\u9FA5A-Za-z0-9]+$' },
				{ title: 'Email表达式', pattern: "[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?" },
				{ title: 'url表达式', pattern: '[a-zA-z]+://[^\s]*' },
				{ title: '国内电话号码', pattern: '\d{3}-\d{8}|\d{4}-\{7,8}' },
				{ title: '腾讯QQ号', pattern: '[1-9][0-9]{4,}' },
				{ title: '中国邮政编码', pattern: '[1-9]\d{5}(?!\d)' },
				{ title: '18位身份证号', pattern: '^(\d{6})(\d{4})(\d{2})(\d{2})(\d{3})([0-9]|X)$' }
			]
		};
	},
	methods: {
		openResult(item) {
			if (item.status == 3 || item.type == 1) {
				this.$message({
					message: item.type == 1 ? '提醒任务无结果' : '任务未开始',
					type: 'warning',
					center: true
				});
			} else {
				this.taskResult.item = item;
				this.taskResult.format = item.saveMsg;
				this.$http.post('selectResult', { m_ID: item.m_ID }, { emulateJSON: true }).then(
					data => {
						if (data.body.code == 200) {
							this.taskResult.data = data.body.data;
							this.showTaskResult = true;
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
		downLoad() {
			let blob = sheet2blob(XLSX.utils.json_to_sheet(this.$data.taskResult.data));
			openDownloadDialog(blob, this.$data.taskResult.item.name+'.xlsx');
		},
		listSuccess(response, file, fileList) {
			if (response.code == 200) {
				if (fileList.length > 1) {
					this.$refs.upload.uploadFiles.splice(0, 1);
				}
				this.$data.taskForm.groupList = response.data;
			} else if (response.code == 314) {
				this.$message({
					type: 'warning',
					message: '请选择文件上传'
				});
			} else {
				this.$message({
					type: 'error',
					message: '未知错误'
				});
			}
		},
		listError(err, file, fileList) {
			this.$message({
				type: 'error',
				message: '未知错误'
			});
		},
		listRemove(file, fileList) {
			this.$data.taskForm.groupList = '';
		},
		onSubmit() {
			this.$refs.taskForm.validate(valid => {
				if (valid) {
					if (this.$props.id != null) {
						let idList = this.$props.id.split('/');
						let id = idList[idList.length - 1];
						let taskForm = this.$data.taskForm;
						let httpMission = {
							m_ID: taskForm.m_ID,
							name: taskForm.name,
							isPrivate: taskForm.isPrivate,
							endMsg: taskForm.endMsg,
							msg: taskForm.msg,
							regex: taskForm.regex,
							reply: taskForm.reply,
							remindMsg: taskForm.remindMsg,
							remindIsPrivate: taskForm.remindIsPrivate,
							remindTime: taskForm.remindTime,
							saveMsg: taskForm.saveMsg,
							groupID: id,
							status: taskForm.status,
							groupList: taskForm.groupList,
							isRepeat: taskForm.isRepeat,
							repeatTime: taskForm.repeatTime,
							type: taskForm.type
						};
						if (taskForm.type == 2) {
							httpMission['startTime'] = new Date(taskForm.time[0]).getTime();
							httpMission['endTime'] = new Date(taskForm.time[1]).getTime();
							if (taskForm.endMsg == '') {
								httpMission['endMsg'] = (taskForm.name || "'任务'") + '结束';
							}
						} else if (taskForm.type == 1) {
							httpMission['startTime'] = new Date(taskForm.time).getTime();
							httpMission['endTime'] = new Date(taskForm.endTime).getTime();
						}
						if (this.taskForm.dialogButtonName == '创建') {
							this.$http.post('insertHttpMission', { httpMission: JSON.stringify(httpMission) }, { emulateJSON: true }).then(
								data => {
									if (data.body.code == 200) {
										for (let j = 0; data.body.data.saveMsg != null && j < data.body.data.saveMsg.length; j++) {
											data.body.data.saveMsg[j] = parseInt(data.body.data.saveMsg[j]);
										}
										data.body.data.type = data.body.data.type + '';
										this.$data.allTask.push(data.body.data);
										this.$message({
											type: 'success',
											message: '添加成功'
										});
										this.$data.formPage = false;
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
							this.$http.post('updateHttpMission', { httpMission: JSON.stringify(httpMission) }, { emulateJSON: true }).then(
								data => {
									if (data.body.code == 200) {
										for (let j = 0; data.body.data.saveMsg != null && j < data.body.data.saveMsg.length; j++) {
											data.body.data.saveMsg[j] = parseInt(data.body.data.saveMsg[j]);
										}
										data.body.data.type = data.body.data.type + '';
										for (let i = 0; i < this.$data.allTask.length; i++) {
											if (this.$data.allTask[i].m_ID == data.body.data.m_ID) {
												this.$data.allTask.splice(i, 1, data.body.data);
											}
										}
										this.$message({
											type: 'success',
											message: '修改成功'
										});
										this.$data.formPage = false;
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
					}
				} else {
					return false;
				}
			});
		},
		deleteTask(index, row) {
			this.$http.post('deleteHttpMission', { m_ID: row.m_ID }, { emulateJSON: true }).then(
				data => {
					if (data.body.code == 200) {
						for (let i = 0; i < this.$data.allTask.length; i++) {
							if (this.$data.allTask[i].m_ID == row.m_ID) {
								this.$data.allTask.splice(i, 1);
								this.$message({
									type: 'success',
									message: '删除成功'
								});
								break;
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
		changeTestPattern() {
			try {
				var reg = new RegExp(this.myTestPattern.pattern, 'gm');
				this.myTestPattern.result = reg.exec(this.myTestPattern.text)[0];
			} catch (e) {
				this.$message({
					dangerouslyUseHTMLString: true,
					message: '正则表达式存在错误' + '<br/><br/>' + e.message,
					type: 'warning',
					center: true
				});
			}
		},
		insertPattern() {
			this.$data.taskForm.regex = this.$data.myTestPattern.pattern;
		},
		createTask(data) {
			if (this.$props.id != null) {
				let idList = this.$props.id.split('/');
				let id = idList[idList.length - 1];
				let taskForm = this.$data.taskForm;
				this.taskForm.groupID = id;
			} else {
				this.taskForm.groupID = '';
			}
			this.taskForm.m_ID = '';
			this.taskForm.name = '';
			this.taskForm.isPrivate = false;
			this.taskForm.endMsg = '';
			this.taskForm.msg = '';
			this.taskForm.regex = '';
			this.taskForm.reply = '';
			this.taskForm.remindMsg = '';
			this.taskForm.remindIsPrivate = false;
			this.taskForm.remindTime = '';
			this.taskForm.saveMsg = [];
			this.taskForm.groupList = '';
			this.taskForm.isRepeat = false;
			this.taskForm.repeatTime = '';
			this.taskForm.type = '1';
			this.taskForm.status = 0;
			this.myTestPattern.text = '';
			this.myTestPattern.pattern = '';
			this.myTestPattern.result = '';
			this.taskForm.dialogButtonName = '创建';
			this.taskForm.time = [];
			if (data != null) {
				this.taskForm.m_ID = data.m_ID;
				this.taskForm.name = data.name;
				this.taskForm.isPrivate = data.isPrivate;
				this.taskForm.endMsg = data.endMsg;
				this.taskForm.msg = data.msg;
				this.taskForm.regex = data.regex;
				this.taskForm.reply = data.reply;
				this.taskForm.remindMsg = data.remindMsg;
				this.taskForm.remindIsPrivate = data.remindIsPrivate;
				this.taskForm.remindTime = data.remindTime;
				this.taskForm.saveMsg = data.saveMsg;
				this.taskForm.groupList = data.groupList;
				this.taskForm.isRepeat = data.isRepeat;
				this.taskForm.repeatTime = data.repeatTime;
				this.taskForm.type = data.type;
				this.taskForm.status = data.status;
				this.taskForm.dialogButtonName = '修改';
				window.setTimeout(() => {
					this.taskForm.time = data.type == 1 ? data.startTime : [data.startTime, data.endTime];
				}, 50);
			}
			this.formPage = true;
		}
		// stopTask(index, item) {
		// 	console.log(index, item);
		// },
		// restartTask(index, item) {
		// 	console.log(index, item);
		// },
		// startTask(index, item) {
		// 	console.log(index, item);
		// },
	},
	mounted() {
		this.$router.app.$data.routerMap = calcPath(this.$props.id.split('/'), this.$props.path.split('/'), this.$props.type);
		if (this.$props.id != null) {
			let idList = this.$props.id.split('/');
			let id = idList[idList.length - 1];
			this.$http.post('getHttpMission', { groupId: id }, { emulateJSON: true }).then(
				data => {
					if (data.body.code == 200) {
						this.$data.allTask = data.body.data;
						for (let i = 0; i < this.$data.allTask.length; i++) {
							for (let j = 0; this.$data.allTask[i].saveMsg != null && j < this.$data.allTask[i].saveMsg.length; j++) {
								this.$data.allTask[i].saveMsg[j] = parseInt(this.$data.allTask[i].saveMsg[j]);
							}
							this.$data.allTask[i].type = this.$data.allTask[i].type + '';
						}
					} else if (data.body.code == 314) {
						this.$message({
							type: 'error',
							message: '群不存在'
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
		}
	},
	props: {
		id: {
			type: String
		},
		path: {
			type: String
		},
		type: {
			type: String
		}
	},
	components: {
		TipButton,
		MyPopconfirm
	},
	computed: {
		filterTask() {
			if (this.selectTab == "-1") {
				return this.allTask;
			} else {
				var task = [];
				this.allTask.forEach((val, index) => {
					if (val.status == this.selectTab) task.push(val);
				});
				return task;
			}
		},
		saveDateList() {
			var data = [];
			var matcher = /\(.[^()]*\)/;
			for (var i = 0, j = 0; i < this.$data.taskForm.regex.length; j++) {
				var tmp = matcher.exec(this.$data.taskForm.regex.substr(i));
				if (tmp != null) {
					data.push({ key: j, label: tmp[0], disabled: false });
				}
				i += tmp != null ? tmp.index + tmp[0].length : this.$data.taskForm.regex.length;
			}
			return data;
		}
	},
	watch: {
		'taskForm.type': function(newVal, oldVal) {
			this.$data.taskForm.time = '';
		}
	}
};
</script>

<style scoped="scoped">
.el-tabs--border-card {
	border: none;
}
.el-card.success {
	border-left: 5px solid #67c23a;
}
.el-card.warning {
	border-left: 5px solid #e6a23c;
}
.el-card.danger {
	border-left: 5px solid #f56c6c;
}
.el-card.info {
	border-left: 5px solid #909399;
}
.el-card {
	margin-top: 5px;
}
.timePicker {
	/* float: right; */
	margin-left: calc(50% - 430px);
}
.tipPattern h3 {
	font-size: 18px;
	line-height: 1.8;
}
.tipPattern p {
	font-size: 14px;
	line-height: 1.7;
}
</style>
