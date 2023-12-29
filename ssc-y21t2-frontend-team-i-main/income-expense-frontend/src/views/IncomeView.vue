<template>
	<div class="home">
		<Income msg="Income page" />
	</div>
</template>

<script>
// @ is an alias to /src
import Income from "@/components/Income.vue";

export default {
	name: "IncomeView",
	components: {
		Income,
	},
};
</script>
