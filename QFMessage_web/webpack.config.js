const resolve = require('path').resolve
const webpack = require('webpack')
const HtmlWebpackPlugin = require('html-webpack-plugin')
const url = require('url')
const publicPath = ''
const glob = require('glob'); //glob，这个是一个全局的模块，动态配置多页面会用得着

function getEntry() {
	var entry = {
		vendor: "./src/vendor.js",
	};
	//读取src目录所有page入口
	glob.sync('./src/**/*.js').forEach(function(name) {
		var start = name.indexOf('src/') + 4;
		var end = name.length - 3;
		var eArr = [];
		var n = name.slice(start, end);
		n = n.split('/')[0];
		if (n !== "assets") {
			eArr.push(name);
			entry[n] = eArr;
		}
	})
	return entry;
}

// var getHtmlConfig = function(name, chunks) {
// 	return {
// 		template: `./src/pages/${name}.html`,
// 		filename: `pages/${name}.html`,
// 		inject: true,
// 		hash: false,
// 		chunks: [name]
// 	}
// }

module.exports = (options = {}) => ({
	entry: getEntry(),
	output: {
		path: resolve(__dirname, 'dist'),
		filename: options.dev ? '[name].js' : '[name].js?[chunkhash]',
		chunkFilename: '[id].js?[chunkhash]',
		publicPath: options.dev ? '/assets/' : publicPath
	},
	module: {
		rules: [{
				test: /\.vue$/,
				use: ['vue-loader']
			},
			{
				test: /\.js$/,
				loader: 'babel-loader',
				options: {
					presets: ['es2015']
				},
				include: [resolve('src')]
			},
			{
				test: /\.css$/,
				use: ['style-loader', 'css-loader', 'postcss-loader'],
			},
			{
				test: /\.(png|jpg|jpeg|gif|eot|ttf|woff|woff2|svg|svgz)(\?.+)?$/,
				use: [{
					loader: 'url-loader',
					options: {
						limit: 10000,
						name: "img/[hash:32].[ext]"
					}
				}]
			},
			// {
			// 	test: /\.(eot|ttf|woff|woff2|svg|svgz)(\?.+)?$/,
			// 	use: [{
			// 		loader: 'url-loader',
			// 		options: {
			// 			limit: 0,
			// 			name: 'fonts/[hash:32].[ext]'
			// 		},
			// 	}]
			// }
		]
	},
	plugins: [
		new webpack.optimize.CommonsChunkPlugin({
			names: ['vendor', 'manifest']
		}),
		new HtmlWebpackPlugin({
			template: 'src/index/index.html',
			filename: 'index.html',
			favicon: 'src/assets/logo.ico',
			minify: {
				minimize: true, //是否打包为最小值
				removeAttrbuteQuotes: true, //去除引号
				removeComments: true, //去掉注释
				collapseWhitespace: true, //去掉空格
				minifyCss: true, //压缩css
				removeEmptyElements: false, //清理内容为空的元素
			},
			inject: true,
			chunks: ['vendor', 'manifest', 'index'],
			hash: true //引入产出的资源时加上哈希避免缓存
		}),
		new HtmlWebpackPlugin({
			template: 'src/login/login.html',
			filename: 'login.html',
			favicon: 'src/assets/logo.ico',
			minify: {
				minimize: true, //是否打包为最小值
				removeAttrbuteQuotes: true, //去除引号
				removeComments: true, //去掉注释
				collapseWhitespace: true, //去掉空格
				minifyCss: true, //压缩css
				removeEmptyElements: false, //清理内容为空的元素
			},
			inject: true,
			chunks: ['vendor', 'manifest', 'login'],
			hash: true //引入产出的资源时加上哈希避免缓存
		}),
		new HtmlWebpackPlugin({
			template: 'src/register/register.html',
			filename: 'register.html',
			favicon: 'src/assets/logo.ico',
			minify: {
				minimize: true, //是否打包为最小值
				removeAttrbuteQuotes: true, //去除引号
				removeComments: true, //去掉注释
				collapseWhitespace: true, //去掉空格
				minifyCss: true, //压缩css
				removeEmptyElements: false, //清理内容为空的元素
			},
			inject: true,
			chunks: ['vendor', 'manifest', 'register'],
			hash: true //引入产出的资源时加上哈希避免缓存
		}),
		new HtmlWebpackPlugin({
			template: 'src/user/user.html',
			filename: 'user/user.html',
			favicon: 'src/assets/logo.ico',
			minify: {
				minimize: true, //是否打包为最小值
				removeAttrbuteQuotes: true, //去除引号
				removeComments: true, //去掉注释
				collapseWhitespace: true, //去掉空格
				minifyCss: true, //压缩css
				removeEmptyElements: false, //清理内容为空的元素
			},
			inject: true,
			chunks: ['vendor', 'manifest', 'user'],
			hash: true //引入产出的资源时加上哈希避免缓存
		}),
	],
	resolve: {
		alias: {
			'~': resolve(__dirname, 'src')
		},
		extensions: ['.js', '.vue', '.json', '.css']
	},
	devServer: {
		host: '127.0.0.1',
		port: 8010,
		proxy: {
			'/assets':{
				target: 'http://localhost/QFMessageServer_war_exploded',
				changeOrigin: true,
				pathRewrite: {
					'^/assets':''
				}
			},
			'/api': {
				target: 'http://localhost/QFMessageServer_war_exploded',
				changeOrigin: true,
				pathRewrite: {
					'^/assets':''
				}
			}
		},
		historyApiFallback: {
			index: url.parse(options.dev ? '/assets/' : publicPath).pathname,
		}
	},
	devtool: options.dev ? '#eval-source-map' : '#source-map'
})
