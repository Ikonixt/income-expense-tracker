import Vue from "vue";
import Vuex from "vuex";

Vue.use(Vuex);

export default new Vuex.Store({
	state: {
		loggedIn: false,
		username: null,
		userId: null,
	},
	getters: {},
	mutations: {
		setLoggedIn(state, loggedIn) {
			state.loggedIn = loggedIn;
		},
		setUsername(state, username) {
			state.username = username;
		},
		setUserId(state, userId) {
			state.userId = userId;
		},
	},
	actions: {
		// payload: the actual data pack that is sent with the GET method in HTTP
		setLoggedInUser({ commit }, payload) {
			commit("setLoggedIn", payload.loggedIn);
			commit("setUsername", payload.username);
			commit("setUserId", payload.userId);
		},
		clearUser({ commit }) {
			commit("setLoggedIn", false);
			commit("setUsername", null);
			commit("setUserId", null);
		},
	},
	modules: {},
});
