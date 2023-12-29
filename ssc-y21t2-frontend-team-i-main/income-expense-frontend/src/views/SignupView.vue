<template>
	<v-container>
		<div class="signup">
			<Signup msg="Signup page" />
		</div>
	</v-container>
</template>

<script>
// @ is an alias to /src
import Signup from "@/components/Signup.vue";

export default {
	name: "SignupView",
	components: {
		Signup,
	},
};
</script>
