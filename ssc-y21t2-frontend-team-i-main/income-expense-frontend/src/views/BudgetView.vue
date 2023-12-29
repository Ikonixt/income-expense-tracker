<template>
	<v-container>
		<div class="home">
			<Budget msg="Overview page" />
		</div>
	</v-container>
</template>

<script>
// @ is an alias to /src
import Budget from "@/components/Budget.vue";

export default {
	name: "BudgetView",
	components: {
		Budget,
	},
};
</script>
