import Vue from "vue";
import VueRouter from "vue-router";
import OverviewView from "../views/OverviewView.vue";
import BudgetView from "../views/BudgetView.vue";
import IncomeView from "../views/IncomeView.vue";
import ExpenseView from "../views/ExpenseView.vue";
import RetirementView from "../views/RetirementView.vue";
import LoginView from "../views/LoginView.vue";
import SignupView from "../views/SignupView.vue";
import store from "@/store";

Vue.use(VueRouter);

const routes = [
	{
		path: "/",
		name: "overview",
		component: OverviewView,
		meta: {
			requiresAuth: true,
		},
	},
	{
		path: "/about",
		name: "about",
		// route level code-splitting
		// this generates a separate chunk (about.[hash].js) for this route
		// which is lazy-loaded when the route is visited.
		component: () =>
			import(/* webpackChunkName: "about" */ "../views/AboutView.vue"),
		meta: {
			requiresAuth: true,
		},
	},
	{
		path: "/budget",
		name: "budget",
		component: BudgetView,
		meta: {
			requiresAuth: true,
		},
	},
	{
		path: "/income",
		name: "income",
		component: IncomeView,
		meta: {
			requiresAuth: true,
		},
	},
	{
		path: "/expense",
		name: "expense",
		component: ExpenseView,
		meta: {
			requiresAuth: true,
		},
	},
	{
		path: "/retirement",
		name: "retirement",
		component: RetirementView,
		meta: {
			requiresAuth: true,
		},
	},
	{
		path: "/login",
		name: "login",
		component: LoginView,
	},
	{
		path: "/signup",
		name: "signup",
		component: SignupView,
	},
	{
		path: "*",
		name: "404",
		component: {
			template: "<p>Page Not Found</p>",
		},
	},
];

const router = new VueRouter({
	routes,
});

router.beforeEach(async (to, from, next) => {
	// get login state with whoami and axios
	let response = await Vue.axios.get("/api/whoami");
	// set the states according to the payload
	// response.data is a "payload" containing the user data
	await store.dispatch("setLoggedInUser", response.data);
	// get the login state
	let loggedIn = store.state.loggedIn;
	// if the route is "login" and the user is currently logged in
	if (to.name === "login" && loggedIn) {
		// redirect to overview page
		next({ name: "overview" });
	}
	// check if the route requires authentication
	if (to.matched.some((record) => record.meta.requiresAuth)) {
		// if not logged in
		if (!loggedIn) {
			// redirect to login page
			next({ name: "login" });
		}
	} else {
		// if the route does not require authentication
		// if not exists and not logged in
		if (to.name === "404" && !loggedIn) {
			// redirect to login page
			next({ name: "login" });
		}
	}
	// otherwise, go to wherever you are going
	// make sure to always call next()
	next();
});

export default router;
