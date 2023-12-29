<template>
	<div class="job">
		<div class="jobText">
			<hr />
			<div style="padding: 15px">
				<div>
					<p style="color: red">{{ checkIfOverbudget }}</p>
					<h4 id="nameText">{{ subBudgetName }}</h4>
					<br />
					<h4 id="usageText">Current:</h4>
					<p>{{ subBudgetCurrentAmount }}</p>
					<br />
					<h4 id="usageText">Limit:</h4>
					<p>{{ subBudgetLimit }}</p>
				</div>

				<button class="button mr-4" @click="activateEdit">Edit</button>
				<button class="deleteButton" @click="activateDeleteDialog">
					Delete
				</button>
			</div>

			<div class="modal" v-if="deleteVisible">
				<div class="dialog">
					Are you sure you want to delete this Sub Budget?
					<br />
					<button
						class="deleteButton"
						@click="sendSubBudgetIdtoDelete"
					>
						Delete
					</button>
					<br />
					<button class="normalButton" @click="hideDeletePopup">
						Dismiss
					</button>
				</div>
			</div>

			<div class="modal" v-if="editVisible">
				<div class="dialog">
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
			<hr />
		</div>
	</div>
</template>

<script>
export default {
	props: [
		"subBudgetId",
		"subBudgetName",
		"subBudgetLimit",
		"subBudgetCurrentAmount",
		"budgetId",
	],
	data() {
		return {
			deleteVisible: false,
			editVisible: false,
			newAllocation: 0,
			overbudget: "",
		};
	},
	computed: {
		checkIfOverbudget: function () {
			if (this.subBudgetCurrentAmount > this.subBudgetLimit) {
				return "Overbudget";
			}
			return "";
		},
	},
	methods: {
		// checkIfOverbudget() {
		// 	console.log(this.subBudgetLimit);
		// 	if (this.subBudgetCurrentAmount > this.subBudgetLimit) {
		// 		return "Overbudget";
		// 	}
		// },
		activateDeleteDialog() {
			this.deleteVisible = true;
		},
		hideDeletePopup() {
			this.deleteVisible = false;
		},
		sendReallocate() {
			if (this.newAllocation < 0) {
				console.log("Allocation cannot be negative");
			}
			this.$emit("subbudget-reallocate", {
				id: this.subBudgetId,
				newAlloc: this.newAllocation,
			});
			this.newAllocation = 0;
			this.hideEditPopup();
		},
		activateEdit() {
			this.editVisible = true;
		},
		hideEditPopup() {
			this.editVisible = false;
		},
		sendSubBudgetIdtoDelete() {
			this.$emit("subbudget-id", this.subBudgetId);
			this.hideDeletePopup();
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
</style>
