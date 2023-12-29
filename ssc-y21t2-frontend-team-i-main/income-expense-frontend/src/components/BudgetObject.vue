<template>
	<div class="job section">
		<div class="jobText">
			<div>
				<p style="color: red">{{ checkIfOverbudget }}</p>
				<h1 id="nameText">{{ budgetName }}</h1>
				<br />
				<h3 id="usageText">Current:</h3>
				<p>{{ budgetCurrentAmount }}</p>
				<h3 id="usageText">Limit:</h3>
				<p>{{ budgetLimit }}</p>
			</div>

			<button class="button mr-4" @click="sendSelectedBudgetId">
				View
			</button>
			<button class="button mr-4" @click="activateEdit">Edit</button>
			<button class="deleteButton" @click="activateDeleteDialog">
				Archive
			</button>

			<div class="deleteModal" v-if="deleteVisible">
				<div class="deleteDialog" style="color: black">
					Are you sure you want to archive this Budget?
					<br />
					<button class="deleteButton" @click="archiveBudget">
						Archive
					</button>
					<br />
					<button class="normalButton" @click="hideDeletePopup">
						Dismiss
					</button>
				</div>
			</div>

			<div class="modal" v-if="editVisible">
				<div class="dialog" style="color: black">
					<form @submit.prevent="sendReallocate">
						<p><b>Allocation: </b></p>
						<input
							id="newAllocation"
							type="number"
							v-model="newAllocation"
							required
						/>
						<br />
						<button class="button">Ok</button>
						<br />
					</form>
					<button class="button" @click="hideEditPopup">
						Dismiss
					</button>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
export default {
	props: [
		"budgetId",
		"budgetName",
		"budgetLimit",
		"budgetType",
		"budgetCurrentAmount",
	],
	data() {
		return {
			deleteVisible: false,
			editVisible: false,
			newAllocation: 0,
		};
	},
	computed: {
		checkIfOverbudget: function () {
			if (this.budgetCurrentAmount > this.budgetLimit) {
				return "Overbudget";
			}
			return "";
		},
	},
	methods: {
		sendReallocate() {
			if (this.newAllocation < 0) {
				console.log("Allocation cannot be negative");
			}
			this.$emit("budget-reallocate", {
				id: this.budgetId,
				newAlloc: this.newAllocation,
			});
			this.newAllocation = 0;
			this.hideEditPopup();
		},
		activateEdit() {
			this.editVisible = true;
			// console.log(this.editVisible);
		},
		hideEditPopup() {
			this.editVisible = false;
		},
		activateDeleteDialog() {
			this.deleteVisible = true;
		},
		hideDeletePopup() {
			this.deleteVisible = false;
		},
		sendSelectedBudgetId() {
			this.$emit("budget-id", this.budgetId);
			this.hideDeletePopup();
		},
		archiveBudget() {
			this.$emit("archive-budget-id", this.budgetId);
		},
	},
};
</script>

<style scoped>
.button {
	border: none;
	font-family: Helvetica;
	float: center;
	background-color: grey;
	border-radius: 5px;
	padding: 5px 20px;
	text-align: center;
	text-decoration: none;
	display: inline-block;
	font-size: 16px;
	transition-duration: 0.2s;
	color: white;
	margin-bottom: 5px;
}

.button:hover {
	background-color: lightgray;
	color: white;
}
.deleteButton {
	border: none;
	font-family: Helvetica;
	float: center;
	background-color: #d43b3b;
	border-radius: 5px;
	padding: 5px 20px;
	text-align: center;
	text-decoration: none;
	display: inline-block;
	font-size: 16px;
	transition-duration: 0.2s;
	color: white;
}

.deleteButton:hover {
	background-color: #ff6962;
	color: white;
}

.deleteDialog {
	font-family: Helvetica;
	font-size: 18px;
	background: white;
	border-radius: 5px;
	border: none;
	z-index: 10;
	width: 25%;
	height: 50%;
	padding-top: 5.5%;
	text-align: center;
}

.deleteModal {
	/* Modified from https://www.w3schools.com/howto/howto_css_modals.asp */
	position: fixed;
	top: 0;
	left: 0;
	z-index: 10;
	height: 100%;
	width: 100%;
	background-color: rgba(0, 0, 0, 0.5);
	display: flex;
	justify-content: center;
	align-items: center;
}

.modal {
	/* Modified from https://www.w3schools.com/howto/howto_css_modals.asp */
	position: fixed;
	top: 0;
	left: 0;
	z-index: 10;
	height: 100%;
	width: 100%;
	background-color: rgba(0, 0, 0, 0.5);
	display: flex;
	justify-content: center;
	align-items: center;
}

.dialog {
	font-family: Helvetica;
	font-size: 18px;
	background: white;
	border-radius: 5px;
	border: none;
	z-index: 10;
	width: 25%;
	height: 50%;
	padding-top: 5.5%;
	text-align: center;
}

.header {
	border-style: solid;
}
</style>
