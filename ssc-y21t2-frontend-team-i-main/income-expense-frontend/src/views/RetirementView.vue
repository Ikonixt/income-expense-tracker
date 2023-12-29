<template>
	<v-container>
		<div class="retirement">
			<Retirement msg="Retirement page" />
		</div>
	</v-container>
</template>

<script>
// @ is an alias to /src
import Retirement from "@/components/Retirement.vue";

export default {
	name: "RetirementView",
	components: {
		Retirement,
	},
};
</script>
