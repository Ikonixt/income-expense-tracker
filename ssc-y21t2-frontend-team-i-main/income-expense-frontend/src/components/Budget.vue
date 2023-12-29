<template>
	<div class="app">
		<v-row>
			<v-col cols="12" md="12">
				<h1>Budget Page</h1>
			</v-col>
			<v-col cols="12" md="12">
				<div class="section">
					<h3>Available Budgets</h3>
					<br />
					<v-btn color="success" @click="activateAddBudget">
						Add
					</v-btn>
				</div>
			</v-col>

			<div class="addBudgetModal" v-if="addBudgetVisible">
				<div class="addBudgetDialog">
					<div class="createBudgetFormContainer">
						<!-- Create Budget Form -->
						<form @submit.prevent="addNewBudget">
							<div align="left" class="px-4">
								<v-row>
									<v-col cols="12" md="6">
										<label for="newBudgetName"
											>Name:
										</label>
									</v-col>
									<v-col cols="12" md="6">
										<v-text-field
											id="newBudgetName"
											type="text"
											v-model="newBudgetName"
											style="margin-top: -15px"
											light
											required
										/>
									</v-col>

									<v-col cols="12" md="6">
										<label for="newBudgetType">
											Type:
										</label>
									</v-col>
									<v-col cols="12" md="6">
										<select
											class="custom-select"
											v-model="newBudgetType"
										>
											<option value="Simple" selected>
												Simple
											</option>
											<option value="Custom">
												Custom
											</option>
										</select>
									</v-col>
									<v-col cols="12" md="6">
										<label for="newBudgetAmount"
											>Amount:
										</label>
									</v-col>
									<v-col cols="12" md="6">
										<v-text-field
											id="newBudgetAmount"
											type="number"
											v-model="newBudgetAmount"
											style="margin-top: -15px"
											light
											required
										/>
									</v-col>
								</v-row>
							</div>
							<br />
							<button class="button">Add</button>
						</form>
					</div>

					<br />
					<button class="button" @click="hideAddBudget">Back</button>
				</div>
			</div>
			<v-col cols="12" md="12">
				<div class="budgetObjectContainer">
					<budget-object
						@budget-reallocate="reallocateBudget"
						@archive-budget-id="archiveBudget"
						@budget-id="displayBudgetInfo"
						v-for="budget in myAvailableBudget"
						:key="budget.budgetId"
						:budgetId="budget.budgetId"
						:budgetName="budget.budgetName"
						:budgetLimit="budget.budgetLimit"
						:budgetType="budget.budgetType"
						:budgetCurrentAmount="budget.budgetCurrentAmount"
					>
					</budget-object>
				</div>
			</v-col>
		</v-row>
		<br />
		<div class="col-container" v-if="budgetViewerVisible">
			<div class="section">
				<h1>
					<b>{{ selectedBudgetName }}</b>
				</h1>
				<p><b>Budget: </b> {{ selectedBudgetLimit }}</p>
				<p><b>Current: </b> {{ selectedBudgetCurrentAmount }}</p>
				<h2>Allocation</h2>
				<div>
					<v-btn color="success" @click="activateAddSubBudget">
						Add
					</v-btn>
				</div>

				<div class="addBudgetModal" v-if="addSubBudgetVisible">
					<div class="addBudgetDialog">
						<div class="addSubBudgetFormContainer">
							<!-- Create Budget Form -->
							<form @submit.prevent="addNewSubBudget">
								<div align="left" class="px-4">
									<v-row>
										<v-col cols="12" md="6">
											<label for="newSubBudgetSourceId">
												Type:
											</label>
										</v-col>
										<v-col cols="12" md="6">
											<select
												class="custom-select"
												v-model="newSubBudgetSourceId"
											>
												<option
													v-for="s in myExpenseSources"
													:key="s.id"
													:value="s.sourceId"
												>
													{{ s.sourceName }}
												</option>
												<option
													value=""
													selected
													disabled
													hidden
												>
													Choose here
												</option>
											</select>
										</v-col>
										<v-col cols="12" md="6">
											<label for="newSubBudgetAllocation"
												>Allocation:
											</label>
										</v-col>
										<v-col cols="12" md="6">
											<v-text-field
												id="newSubBudgetAllocation"
												type="number"
												v-model="newSubBudgetAllocation"
												style="margin-top: -15px"
												light
												required
											/>
										</v-col>
									</v-row>
								</div>
								<br />
								<button class="button">Add</button>
							</form>
						</div>

						<br />
						<button class="button" @click="hideAddSubBudget">
							Back
						</button>
					</div>
				</div>
				<br />

				<div class="subBudgetContainer">
					<sub-budget-object
						@subbudget-reallocate="reallocateSubBudget"
						@subbudget-id="deleteSubBudget"
						v-for="subbudget in myCurrentSubBudgets"
						:key="subbudget.subBudgetId"
						:subBudgetId="subbudget.subBudgetId"
						:subBudgetName="subbudget.subBudgetName"
						:subBudgetLimit="subbudget.subBudgetAllocation"
						:subBudgetCurrentAmount="
							subbudget.subBudgetCurrentAmount
						"
						:budgetId="selectedBudgetId"
					>
					</sub-budget-object>
				</div>
			</div>
			<div class="section">
				<h2>Budget Logs</h2>
				<div class="subBudgetLogContainer">
					<log-object
						@delete-log="deleteSubBudgetLog"
						v-for="log in myBudgetLogs"
						:key="log.logId"
						:logName="log.logName"
						:logAmount="log.logAmount"
						:logDate="log.logDate"
						:logNote="log.logNote"
						:sourceType="log.sourceType"
						:sourceId="log.sourceId"
						:budgetId="log.budgetId"
						:logId="log.logId"
					></log-object>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
import Vue from "vue";
import BudgetObject from "./BudgetObject.vue";
import SubBudgetObject from "./SubBudgetObject.vue";
import LogObject from "./LogObject.vue";

export default {
	name: "Budget",
	props: {
		msg: String,
	},
	components: {
		BudgetObject,
		SubBudgetObject,
		LogObject,
	},
	data() {
		return {
			currentUserId: this.$store.state.userId,
			newBudgetAmount: 0,
			newBudgetType: "Simple",
			newBudgetName: "",
			addBudgetVisible: false,
			budgetViewerVisible: false,

			// Storage arrays and filters
			myExpenseSources: [],
			myAvailableBudget: [],
			myBudgetLogs: [],
			logFilter: "Today",
			onStartup: true,

			// Variables for budget Info section
			selectedBudgetName: "",
			selectedBudgetLimit: 0,
			selectedBudgetCurrentAmount: 0,
			selectedBudgetId: -99,
			selectedBudgetType: "",

			//Variables for new subbudget for budget
			addSubBudgetVisible: false,
			newSubBudgetAllocation: 0,
			newSubBudgetSourceId: -99,

			//Variables for subbudget component
			myCurrentSubBudgets: [],
			myCurrentSubBudgetSources: [],
			budgetAllocated: 0,

			//Current amount for budget and subbudget
			subBudgetCurrentAmountMap: new Map(),
			subBudgetTotalCurrentAmount: 0,
		};
	},
	mounted() {
		this.getMyBudgets();
		this.getMyExpenseSources();
	},
	methods: {
		reallocateBudget(emittedBudgetInfo) {
			const params = new URLSearchParams({
				id: emittedBudgetInfo.id,
				alloc: emittedBudgetInfo.newAlloc,
			});
			Vue.axios
				.post(
					"/api/v1/budget/reallocate",
					{},
					{
						params,
					}
				)
				.then((res) => {
					console.log(res);
					this.getMyBudgets();
					this.getSubBudgetforBudget();
					setTimeout(this.getMyBudgetLogs(), 200);
					// this.mapSubBudgetAmount();
					setTimeout(this.mapSubBudgetAmount, 200);
				});
		},
		reallocateSubBudget(emittedSubbudgetInfo) {
			const params = new URLSearchParams({
				id: emittedSubbudgetInfo.id,
				alloc: emittedSubbudgetInfo.newAlloc,
			});
			Vue.axios
				.post(
					"/api/v1/subbudget/reallocate",
					{},
					{
						params,
					}
				)
				.then((res) => {
					console.log(res);
					this.getMyBudgets();
					this.getSubBudgetforBudget();
					setTimeout(this.getMyBudgetLogs(), 200);
					// this.mapSubBudgetAmount();

					setTimeout(this.mapSubBudgetAmount, 200);
				});
		},
		archiveBudget(emittedId) {
			const params = new URLSearchParams({ id: emittedId });
			Vue.axios
				.post(
					"/api/v1/budget/archivebudget",
					{},
					{
						params,
					}
				)
				.then((res) => {
					console.log(res);
					this.getMyBudgets();
					this.mapSubBudgetAmount();
					this.getMyBudgetLogs();
					setTimeout(this.mapSubBudgetAmount, 200);
					this.budgetViewerVisible = false;
				});
		},
		mapSubBudgetAmount() {
			this.subBudgetCurrentAmountMap = new Map();
			console.log("Current sources");
			let subBudgetTotalCurrentAmount = 0;
			console.log(this.myCurrentSubBudgetSources);
			console.log("Budget logs before mapping");
			console.log(this.myBudgetLogs);
			for (const index in this.myBudgetLogs) {
				subBudgetTotalCurrentAmount += parseFloat(
					this.myBudgetLogs[index].logAmount
				);
				if (
					this.myCurrentSubBudgetSources.includes(
						this.myBudgetLogs[index].sourceType
					)
				) {
					if (
						this.subBudgetCurrentAmountMap.has(
							this.myBudgetLogs[index].sourceType
						)
					) {
						let newAmount =
							this.subBudgetCurrentAmountMap.get(
								this.myBudgetLogs[index].sourceType
							) + parseFloat(this.myBudgetLogs[index].logAmount);
						this.subBudgetCurrentAmountMap.set(
							this.myBudgetLogs[index].sourceType,
							newAmount
						);
					} else {
						this.subBudgetCurrentAmountMap.set(
							this.myBudgetLogs[index].sourceType,
							this.myBudgetLogs[index].logAmount
						);
					}
				}

				//Update subbudget with current amount
				for (const index in this.myCurrentSubBudgets) {
					if (
						this.subBudgetCurrentAmountMap.has(
							this.myCurrentSubBudgets[index].subBudgetName
						)
					) {
						this.myCurrentSubBudgets[index].subBudgetCurrentAmount =
							this.subBudgetCurrentAmountMap.get(
								this.myCurrentSubBudgets[index].subBudgetName
							);
					}
				}
			}
			this.selectedBudgetCurrentAmount = subBudgetTotalCurrentAmount;
			console.log(this.subBudgetCurrentAmountMap);
		},
		deleteSubBudgetLog(emittedLogKey) {
			const params = new URLSearchParams({ id: emittedLogKey });
			Vue.axios
				.post(
					"/api/v1/log/deleteLog",
					{},
					{
						params,
					}
				)
				.then((res) => {
					console.log(res);
					this.getMyBudgets();
					this.getSubBudgetforBudget();
					setTimeout(this.getMyBudgetLogs(), 200);
					// this.mapSubBudgetAmount();

					setTimeout(this.mapSubBudgetAmount, 200);
				});
		},
		deleteSubBudget(emittedSubBudgetId) {
			const params = new URLSearchParams({ id: emittedSubBudgetId });
			Vue.axios
				.post(
					"/api/v1/subbudget/deletesubbudget",
					{},
					{
						params,
					}
				)
				.then((res) => {
					console.log(res);
					this.getSubBudgetforBudget();
				});
		},
		activateAddSubBudget() {
			this.addSubBudgetVisible = true;
		},
		hideAddSubBudget() {
			this.addSubBudgetVisible = false;
		},
		addNewSubBudget() {
			if (this.newSubBudgetAllocation < 0) {
				// Display error
				this.hideAddSubBudget();
				return;
			}

			//Subbudget allocation can exceed total limit
			// if (this.checkIfAllocationExceed(this.newSubBudgetAllocation)) {
			// 	console.log("Allocation exceeds budget limit");
			// 	return;
			// }

			for (const index in this.myCurrentSubBudgets) {
				if (
					this.newSubBudgetSourceId ===
					this.myCurrentSubBudgets[index].subBudgetSourceId
				) {
					console.log("Subbudget already exist");
					return;
				}
			}

			Vue.axios
				.post("/api/v1/subbudget/addsubbudget", {
					budgetId: this.selectedBudgetId,
					sourceId: this.newSubBudgetSourceId,
					name: "",
					allocation: this.newSubBudgetAllocation,
					currentAmount: 0,
				})
				.then((res) => {
					console.log(res);
					// this.getMySubBudgets();
					this.newSubBudgetAllocation = 0;
					this.newSubBudgetSourceId = -99;
					this.hideAddSubBudget();
					this.getSubBudgetforBudget();
					setTimeout(this.mapSubBudgetAmount, 200);
				});
		},
		checkIfAllocationExceed(newAllocation) {
			let accumulatedAllocation = 0;
			for (const index in this.myCurrentSubBudgets) {
				accumulatedAllocation += parseFloat(
					this.myCurrentSubBudgets[index].subBudgetAllocation
				);
			}

			if (
				accumulatedAllocation + parseFloat(newAllocation) >
				this.selectedBudgetLimit
			) {
				return true;
			}
			return false;
		},
		displayBudgetInfo(emittedBudgetId) {
			// console.log(emittedBudgetId);
			this.budgetViewerVisible = true;

			for (const index in this.myAvailableBudget) {
				if (
					this.myAvailableBudget[index].budgetId === emittedBudgetId
				) {
					this.selectedBudgetId = emittedBudgetId;
					this.selectedBudgetName =
						this.myAvailableBudget[index].budgetName;
					this.selectedBudgetLimit =
						this.myAvailableBudget[index].budgetLimit;
					this.selectedBudgetCurrentAmount =
						this.myAvailableBudget[index].budgetCurrentAmount;
					this.selectedBudgetType =
						this.myAvailableBudget[index].budgetType;
					this.getSubBudgetforBudget();
					this.getMyBudgetLogs();
					setTimeout(this.mapSubBudgetAmount, 200);
					break;
				}
			}
		},
		activateAddBudget() {
			this.addBudgetVisible = true;
		},
		hideAddBudget() {
			this.addBudgetVisible = false;
		},
		addNewBudget() {
			if (this.newBudgetAmount < 0) {
				// Display error
				this.hideAddBudget();
				return;
			}

			Vue.axios
				.post("/api/v1/budget/addBudget", {
					budgetName: this.newBudgetName,
					budgetType: this.newBudgetType,
					budgetLimit: this.newBudgetAmount,
					userId: this.currentUserId,
				})
				.then((res) => {
					console.log(res);
					this.getMyBudgets();
					this.hideAddBudget();
				});
		},
		getMyBudgets() {
			let retrievedBudgets = [];
			const params = new URLSearchParams([["id", this.currentUserId]]);
			Vue.axios
				.get("/api/v1/budget/getuserbudget", {
					params,
				})
				.then((res) => {
					console.log(res);
					for (const arrIndex in res.data) {
						let budgetObj = {
							budgetId: res.data[arrIndex].budgetId,
							budgetName: res.data[arrIndex].name,
							budgetLimit: res.data[arrIndex].totalLimit,
							budgetType: res.data[arrIndex].budgetType,
							budgetCurrentAmount:
								res.data[arrIndex].currentAmount,
						};
						retrievedBudgets.push(budgetObj);
					}
					this.myAvailableBudget = retrievedBudgets;
					// console.log(this.myAvailableBudget);
				});
		},
		getMyExpenseSources() {
			let retrievedSources = [];
			let sourceNames = [];
			const params = new URLSearchParams([["id", this.currentUserId]]);
			Vue.axios
				.get("/api/v1/source/getusersources", {
					params,
				})
				.then((res) => {
					console.log(res);
					for (const arrIndex in res.data) {
						if (res.data[arrIndex].sourceType == "EXPENSE") {
							let sourceObj = {
								sourceName: res.data[arrIndex].sourceName,
								sourceId: res.data[arrIndex].sourceId,
								sourceUsage: res.data[arrIndex].sourceUsage,
								sourceType: res.data[arrIndex].sourceType,
							};
							sourceNames.push(sourceObj.sourceName);
							retrievedSources.push(sourceObj);
						}
					}
					this.availableSources = sourceNames;
					this.myExpenseSources = retrievedSources;
					// console.log(this.myExpenseSources);
				});
		},
		getSubBudgetforBudget() {
			let retrievedSubBudgets = [];
			let retrievedSubBudgetSources = [];
			const params = new URLSearchParams([["id", this.selectedBudgetId]]);
			Vue.axios
				.get("/api/v1/subbudget/getsubbudgetforbudget", {
					params,
				})
				.then((res) => {
					console.log("SubBudgets");
					console.log(res);
					this.myCurrentSubBudgetSources = [];
					for (const arrIndex in res.data) {
						let subbudgetObj = {
							budgetId: this.selectedBudgetId,
							subBudgetId: res.data[arrIndex].subBudgetId,
							subBudgetName: res.data[arrIndex].name,
							subBudgetAllocation: res.data[arrIndex].allocation,
							subBudgetCurrentAmount:
								res.data[arrIndex].currentAmount,
							subBudgetSourceId:
								res.data[arrIndex].source.sourceId,
						};
						retrievedSubBudgets.push(subbudgetObj);
						retrievedSubBudgetSources.push(
							subbudgetObj.subBudgetName
						);
					}
					this.myCurrentSubBudgetSources = retrievedSubBudgetSources;
					this.myCurrentSubBudgets = retrievedSubBudgets;
					console.log(this.myCurrentSubBudgetSources);
				});
		},
		getMyBudgetLogs() {
			let retrievedLogs = [];
			const params = new URLSearchParams([
				["id", this.currentUserId],
				["type", "EXPENSE"],
				["sortBy", "date"],
			]);
			Vue.axios
				.get("/api/v1/log/getuserlog", {
					params,
				})
				.then((res) => {
					console.log("Logs for Budget");
					console.log(res);
					for (const arrIndex in res.data) {
						if (
							res.data[arrIndex].budget != null &&
							res.data[arrIndex].budget.budgetId ==
								this.selectedBudgetId
						) {
							let logObj = {
								logName: res.data[arrIndex].name,
								logAmount: res.data[arrIndex].amount,
								logDate: res.data[arrIndex].date,
								logNote: res.data[arrIndex].note,
								sourceType:
									res.data[arrIndex].source.sourceName,
								sourceId: res.data[arrIndex].source.sourceId,
								budgetId: res.data[arrIndex].budget.budgetId,
								logId: res.data[arrIndex].logId,
							};
							retrievedLogs.push(logObj);
						}
					}
					this.myBudgetLogs = retrievedLogs;
					console.log("My budget log");
					console.log(this.myBudgetLogs);
				});
		},
	},
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.addBudgetDialog {
	font-family: Helvetica;
	font-size: 18px;
	/* background: white; */
	background-color: whitesmoke;
	color: black;
	border-radius: 15px;
	border: none;
	z-index: 10;
	width: 25%;
	height: 50%;
	padding-top: 5.5%;
	text-align: center;
	overflow: auto;
}

.addBudgetModal {
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
	overflow: auto;
}

.button {
	border: none;
	font-family: Helvetica;
	float: center;
	background-color: grey;
	border-radius: 5px;
	padding: 5px 30px;
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
.header {
	border-style: solid;
}
.col-container {
	display: grid;
	grid-template-columns: repeat(2, 1fr);
	grid-gap: 1rem;
}

div.section {
	border: solid 1px;
	border-radius: 15px;
	padding: 15px;
	overflow: hidden;
}

select.custom-select {
	border-bottom: 1px solid;
}
</style>
