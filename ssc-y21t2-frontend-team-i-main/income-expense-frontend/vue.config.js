const { defineConfig } = require("@vue/cli-service");
module.exports = defineConfig({
	transpileDependencies: true,
	runtimeCompiler: true,
	devServer: {
		// frontend hostname/IP
		host: "128.199.170.72",
		// disableHostCheck: true,
		allowedHosts: "all",
		// frontend port
		port: 8080,
		// no ssl
		https: false,
		proxy: {
			"/api": {
				// proxy everything from frontend http://localhost:8080/api/** to backend at http://localhost:8090/api/**
				// target: "http://localhost:8090",
				target: "http://128.199.170.72:8090",
			},
		},
		headers: {
			// typical header settings
			"Access-Control-Allow-Origin": "*",
			"Access-Control-Allow-Methods":
				"GET, POST, PUT, DELETE, PATCH, OPTIONS",
			"Access-Control-Allow-Headers":
				"X-Requested-With, content-type, Authoriztion",
		},
	},
});
