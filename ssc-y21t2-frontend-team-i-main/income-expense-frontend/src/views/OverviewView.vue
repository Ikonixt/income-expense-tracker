<template>
	<v-container>
		<div class="overview">
			<Overview msg="Overview page" />
		</div>
	</v-container>
</template>

<script>
// @ is an alias to /src
import Overview from "@/components/Overview.vue";

export default {
	name: "OverviewView",
	components: {
		Overview,
	},
};
</script>
