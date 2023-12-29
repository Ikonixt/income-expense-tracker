<template>
	<v-container>
		<v-form ref="form" v-model="valid" lazy-validation>
			<v-text-field
				v-model="username"
				:rules="usernameRules"
				label="Username"
				required
			></v-text-field>

			<v-text-field
				type="password"
				v-model="password"
				:rules="passwordRules"
				label="Password"
				required
			></v-text-field>

			<v-btn
				:disabled="!valid"
				color="success"
				class="mr-4"
				@click="submit"
			>
				Login
			</v-btn>

			<v-btn color="error" class="mr-4" @click="reset"> Reset </v-btn>
			<v-divider class="my-4"></v-divider>
			<v-btn color="primary" class="my-4" @click="redirect('signup')">
				Sign Up
			</v-btn>
		</v-form>
	</v-container>
</template>

<script>
import Vue from "vue";

export default {
	data: () => ({
		valid: true,
		username: "",
		password: "",
		usernameRules: [(input) => !!input || "Username is required"],
		passwordRules: [(input) => !!input || "Password is required"],
	}),
	methods: {
		async submit() {
			// check if the input is valid according to username/password rules
			if (this.$refs.form.validate()) {
				// submit to backend for authentication
				let formData = new FormData();
				formData.append("username", this.username);
				formData.append("password", this.password);

				let response = await Vue.axios.post("/api/login", formData);
				if (response.data.success) {
					// redirect to overview page
					this.$router.push({ name: "overview" });
				}
			}
		},
		reset() {
			this.$refs.form.reset();
		},
		redirect(to) {
			console.log(to);
			this.$router.push({ name: to }).catch(() => {});
		},
	},
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3 {
	margin: 40px 0 0;
}
ul {
	list-style-type: none;
	padding: 0;
}
li {
	display: inline-block;
	margin: 0 10px;
}
a {
	color: #42b983;
}
</style>
