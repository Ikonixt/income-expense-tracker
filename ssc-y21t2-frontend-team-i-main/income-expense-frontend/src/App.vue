<template>
	<div id="app">
		<v-app app>
			<link
				rel="stylesheet"
				type="text/css"
				href="//fonts.googleapis.com/css?family=Varela+Round"
			/>
			<link
				href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"
				rel="stylesheet"
			/>
			<link
				rel="stylesheet"
				href="//cdn.jsdelivr.net/npm/@mdi/font@6.5.95/css/materialdesignicons.min.css"
			/>
			<v-app-bar elevation="0" app>
				<v-app-bar-nav-icon
					@click.native.stop="sideBar = !sideBar"
					class="hidden-sm-and-up"
				></v-app-bar-nav-icon>
				<v-app-bar-title>
					<router-link
						to="/"
						custom
						v-slot="{ navigate }"
						style="cursor: pointer"
					>
						<span
							@click="navigate"
							@keypress.enter="navigate"
							role="link"
							>SSC Project</span
						>
					</router-link>
				</v-app-bar-title>
				<v-spacer></v-spacer>
				<v-toolbar-items class="hidden-xs-only">
					<template v-for="page in pages">
						<v-btn
							id="navLink"
							v-if="page.show($store.state.loggedIn)"
							small
							depressed
							color="transparent"
							:key="page.title"
							router
							:to="page.link"
						>
							<v-icon left> {{ page.icon }} </v-icon>
							{{ page.title }}
						</v-btn>
					</template>
					<v-menu
						v-if="$store.state.loggedIn"
						bottom
						min-width="150px"
						rounded
						offset-y
					>
						<template v-slot:activator="{ on }">
							<v-btn icon x-large v-on="on">
								<v-avatar color="brown" size="36">
									<span class="white--text text-h6">{{
										$store.state.username
											.charAt(0)
											.toUpperCase()
									}}</span>
								</v-avatar>
							</v-btn>
						</template>
						<v-card class="rounded-lg">
							<v-list-item-content class="justify-center">
								<div class="mx-auto text-center">
									<v-avatar color="brown">
										<span class="white--text text-h6">{{
											$store.state.username
												.charAt(0)
												.toUpperCase()
										}}</span>
									</v-avatar>
									<h3 class="mt-2">
										{{ $store.state.username }}
									</h3>
									<v-divider class="my-3"></v-divider>
									<v-btn
										class="py-5"
										small
										depressed
										width="100%"
										@click="toggleThemeMode"
									>
										<v-icon left>
											mdi-circle-half-full
										</v-icon>
										Switch
									</v-btn>
									<v-divider></v-divider>
									<v-btn
										class="py-5"
										@click="logout"
										small
										depressed
										width="100%"
									>
										<v-icon left> mdi-logout </v-icon>
										Log out
									</v-btn>
								</div>
							</v-list-item-content>
						</v-card>
					</v-menu>
				</v-toolbar-items>
			</v-app-bar>
			<v-navigation-drawer v-model="sideBar" temporary app>
				<v-list>
					<template v-for="page in pages">
						<v-list-item
							v-if="page.show($store.state.loggedIn)"
							:key="page.title"
							router
							:to="page.link"
						>
							<v-list-item-action>
								<v-icon left> {{ page.icon }} </v-icon>
							</v-list-item-action>
							<v-list-item-content>
								{{ page.title }}
							</v-list-item-content>
						</v-list-item>
					</template>
					<v-list-item v-if="$store.state.loggedIn" @click="logout">
						<v-list-item-action>
							<v-icon left> mdi-logout </v-icon>
						</v-list-item-action>
						<v-list-item-content> Log out </v-list-item-content>
					</v-list-item>
				</v-list>
			</v-navigation-drawer>
			<v-main>
				<router-view />
			</v-main>
		</v-app>
	</div>
</template>

<script>
import Vue from "vue";

function showIfLoggedIn(toBeShown) {
	function e(loggedIn) {
		return toBeShown ? loggedIn : !loggedIn;
	}
	return e;
}

export default {
	data() {
		return {
			sideBar: false,
			pages: [
				{
					icon: "mdi-home",
					title: "Overview",
					link: "/",
					show: showIfLoggedIn(true),
				},
				{
					icon: "mdi-bitcoin",
					title: "Budget",
					link: "/budget",
					show: showIfLoggedIn(true),
				},
				{
					icon: "mdi-cash-plus",
					title: "Income",
					link: "/income",
					show: showIfLoggedIn(true),
				},
				{
					icon: "mdi-cash-minus",
					title: "Expense",
					link: "/expense",
					show: showIfLoggedIn(true),
				},
				{
					icon: "mdi-sofa-single",
					title: "Retirement",
					link: "/retirement",
					show: showIfLoggedIn(true),
				},
				{
					icon: "mdi-account-plus",
					title: "Sign up",
					link: "/signup",
					show: showIfLoggedIn(false),
				},
				{
					icon: "mdi-login",
					title: "Log in",
					link: "/login",
					show: showIfLoggedIn(false),
				},
			],
		};
	},
	mounted() {
		this.setThemeMode();
	},
	methods: {
		async logout() {
			let response = await Vue.axios.get("/api/logout");
			if (response.data.success) {
				// update the store state
				await this.$store.dispatch("clearUser");
				// redirect to overview page
				this.$router.push({ name: "login" });
			}
		},
		setThemeMode() {
			const theme = localStorage.getItem("dark");
			if (theme) {
				if (theme == "true") {
					this.$vuetify.theme.dark = true;
				} else {
					this.$vuetify.theme.dark = false;
				}
			}
		},
		toggleThemeMode() {
			this.$vuetify.theme.dark = !this.$vuetify.theme.dark;
			localStorage.setItem("dark", this.$vuetify.theme.dark.toString());
		},
	},
};
</script>

<style>
#app {
	/* font-family: "Verdana", Sans-serif; */
	/* font-family: "Tangerine", serif; */
	/* font-family: "Merienda", Helvetica, Arial; */
	/* font-family: -apple-system, BlinkMacSystemFont, sans-serif; */
	/* font-family: "Helvetica Neue", Helvetica, Arial, sans-serif; */
	/* font-family: Avenir, Helvetica, Arial, sans-serif; */
	font-family: "Varela Round";
	-webkit-font-smoothing: antialiased;
	-moz-osx-font-smoothing: grayscale;
	text-align: center;
}

.v-app-bar-title__content {
	width: 200px !important;
}

h1,
h2,
h3 {
	font-family: "Comfortaa", "Century Gothic", Arial;
}

.v-toolbar__content {
	padding: 30px;
}

.v-toolbar__items a#navLink {
	background-color: transparent !important;
}

.v-toolbar__items a#navLink.v-btn--active {
	color: #42b983;
	border-bottom: solid 4px #42b983 !important;
}

.v-toolbar__items a#navLink.v-btn--active::before {
	opacity: 0 !important;
}

::-webkit-scrollbar {
	background-color: #555;
	width: 3px;
}

::-webkit-scrollbar-thumb {
	background-color: whitesmoke;
	border-radius: 5px;
}
</style>
