<template>
	<v-container>
		<div class="login">
			<Login msg="Login page" />
		</div>
	</v-container>
</template>

<script>
// @ is an alias to /src
import Login from "@/components/Login.vue";

export default {
	name: "LoginView",
	components: {
		Login,
	},
};
</script>
