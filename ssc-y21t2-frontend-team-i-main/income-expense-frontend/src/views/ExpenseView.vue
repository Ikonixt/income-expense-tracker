<template>
	<div class="home">
		<Expense msg="Overview page" />
	</div>
</template>

<script>
// @ is an alias to /src
import Expense from "@/components/Expense.vue";

export default {
	name: "ExpenseView",
	components: {
		Expense,
	},
};
</script>
