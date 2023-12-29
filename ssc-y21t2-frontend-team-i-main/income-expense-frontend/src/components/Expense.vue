<template>
	<div class="app">
		<v-row>
			<v-col cols="12" md="12">
				<h1>Expense</h1>
			</v-col>
			<v-col cols="12" md="12">
				<div class="col-container">
					<div class="row-container gap repeat-1">
						<div class="item">
							<div class="sourceArea">
								<p><strong>Add Expense Source</strong></p>
								<v-divider class="my-4"></v-divider>
								<div
									class="expenseSourceModal"
									v-if="sourceManagerVisible"
								>
									<div class="expenseSourceDialog">
										<h3><strong>Manage sources</strong></h3>
										<div class="sourceComponentContainer">
											<source-object
												@source-rename="renameSource"
												@delete-source="deleteSource"
												v-for="source in myExpenseSources"
												:key="source.sourceId"
												:sourceId="source.sourceId"
												:sourceName="source.sourceName"
												:sourceUsage="
													source.sourceUsage
												"
											>
											</source-object>
										</div>

										<br />
										<button
											class="normalButton"
											@click="hideSourceManagerPopup"
										>
											Ok
										</button>
									</div>
								</div>

								<form>
									<v-row>
										<v-col cols="12" md="4" align="start">
											<label for="newSource"
												>Name:
											</label>
										</v-col>
										<v-col cols="12" md="8">
											<v-text-field
												id="newSource"
												type="text"
												v-model="newSourceName"
												style="margin-top: -15px"
												required
											/>
										</v-col>
									</v-row>
									<button
										class="normalButton"
										@click="activateSourceManager"
									>
										Manage sources</button
									>&nbsp;
									<button
										class="normalButton"
										@click="addNewSource"
									>
										Add
									</button>
								</form>
							</div>
						</div>
						<div class="item">
							<form @submit.prevent="addNewLog">
								<p><strong>Add Expense</strong></p>
								<v-divider class="my-4"></v-divider>
								<v-row>
									<v-col cols="12" md="4" align="start">
										<label>Date: </label>
									</v-col>
									<v-col cols="12" md="8">
										<input
											id="newLogDate"
											type="date"
											v-model="newLogDate"
											required
											style="color: inherit"
										/>
									</v-col>
									<v-col cols="12" md="4" align="start">
										<label for="newLogName">Name: </label>
									</v-col>
									<v-col cols="12" md="8">
										<v-text-field
											id="newLogName"
											type="text"
											v-model="newLogName"
											style="margin-top: -15px"
											required
										/>
									</v-col>
									<v-col cols="12" md="4" align="start">
										<label for="newLogSource"
											>Source:
										</label>
									</v-col>
									<v-col cols="12" md="8">
										<select
											class="border"
											v-model="newLogSource"
											style="color: inherit"
										>
											<option
												v-for="s in availableSources"
												:key="s.id"
												:value="s"
												style="color: black"
											>
												{{ s }}
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
									<v-col cols="12" md="4" align="start">
										<label for="newLogBudget"
											>Budget:
										</label>
									</v-col>
									<v-col cols="12" md="8">
										<select
											class="border"
											v-model="newLogBudget"
											style="color: inherit"
										>
											<option
												v-for="b in myAvailableBudget"
												:key="b.id"
												:value="b.budgetId"
												style="color: black"
											>
												{{ b.budgetName }}
											</option>
											<option
												value="-99"
												selected
												style="color: black"
											>
												None
											</option>
										</select>
									</v-col>
									<v-col cols="12" md="4" align="start">
										<label for="newLogAmount"
											>Amount:
										</label>
									</v-col>
									<v-col cols="12" md="8">
										<v-text-field
											id="newLogAmount"
											type="number"
											v-model="newLogAmount"
											style="margin-top: -15px"
											required
										/>
									</v-col>
									<v-col cols="12" md="4" align="start">
										<label for="newLogNote">Note: </label>
									</v-col>
									<v-col cols="12" md="8">
										<v-textarea
											outlined
											label=""
											id="newLogNote"
											type="text"
											v-model="newLogNote"
										></v-textarea>
									</v-col>
								</v-row>
								<button class="normalButton">Add</button>
							</form>
						</div>
					</div>
					<div class="row-container repeat-2">
						<div
							class="item border item_long"
							style="overflow: auto"
						>
							<p><strong>Log</strong></p>
							<v-divider class="my-4"></v-divider>
							<v-row>
								<v-col cols="12" md="4" align="start">
									Filter by:
								</v-col>
								<v-col cols="12" md="8">
									<select
										id="logFilter"
										v-model="logFilter"
										@change="filterByInterval($event)"
										style="color: inherit"
									>
										<option
											value="Today"
											style="color: black"
										>
											Today
										</option>
										<option
											value="This Month"
											style="color: black"
										>
											This Month
										</option>
										<option
											value="All"
											style="color: black"
										>
											All
										</option>
									</select>
								</v-col>
							</v-row>
							<v-divider class="my-4"></v-divider>
							<log-object
								@delete-log="deleteLog"
								v-for="log in logToDisplay"
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
			</v-col>
		</v-row>
	</div>
</template>

<script>
import Vue from "vue";
import LogObject from "./LogObject.vue";
import SourceObject from "./SourceObject.vue";

export default {
	name: "Expense",
	components: {
		LogObject,
		SourceObject,
	},
	props: {
		msg: String,
	},
	data() {
		return {
			myAvailableBudget: [],
			myExpenseLogs: [],
			myExpenseLogsToday: [],
			myExpenseLogsThisMonth: [],
			logToDisplay: this.myExpenseLogsToday,
			myExpenseSources: [],
			//TODO change this to currently logged in user
			currentUserId: this.$store.state.userId,
			totalExpense: 0,
			sourceManagerVisible: false,
			newSourceName: "",

			availableSources: [],
			newLogName: "",
			newLogNote: "",
			newLogDate: "",
			newLogAmount: 0,
			newLogSource: "",
			newLogBudget: "-99",
			logFilter: "Today",

			onStartup: true,
		};
	},
	mounted() {
		this.getMyBudgets();
		this.getMyExpenseLogs();
		this.getMyExpenseSources();
	},
	methods: {
		renameSource(emittedSourceIdandName) {
			const params = new URLSearchParams({
				id: emittedSourceIdandName.sourceId,
				userId: this.currentUserId,
				newName: emittedSourceIdandName.sourceName,
			});
			Vue.axios
				.post(
					"/api/v1/source/renamesource",
					{},
					{
						params,
					}
				)
				.then((res) => {
					console.log(res);
					this.getMyExpenseLogs();
					this.getMyExpenseSources();
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
		filterByInterval(event) {
			this.getMyExpenseLogs();
			// console.log(this.logToDisplay);
			// console.log(this.myExpenseLogs);
			// console.log(this.myExpenseLogsThisMonth);
			// console.log(this.myExpenseLogsToday);

			setTimeout(() => {
				if (event.target.value === "All") {
					this.logFilter = event.target.value;
					this.logToDisplay = this.myExpenseLogs;
					return;
				} else if (event.target.value === "Today") {
					this.logFilter = event.target.value;
					this.logToDisplay = this.myExpenseLogsToday;
					return;
				} else if (event.target.value === "This Month") {
					this.logFilter = event.target.value;
					this.logToDisplay = this.myExpenseLogsThisMonth;
					return;
				}
			}, 100);
		},
		addNewLog() {
			if (this.newLogSource !== "") {
				// console.log("Submitted");
				let newSourceId = -99;
				for (const index in this.myExpenseSources) {
					if (
						this.myExpenseSources[index].sourceName ===
						this.newLogSource
					) {
						newSourceId = this.myExpenseSources[index].sourceId;
					}
				}

				Vue.axios
					.post("/api/v1/log/addLog", {
						logName: this.newLogName,
						logAmount: this.newLogAmount,
						logType: "EXPENSE",
						sourceId: newSourceId,
						logDate: this.newLogDate,
						logNote: this.newLogNote,
						userId: this.currentUserId,
						budgetId: this.newLogBudget,
					})
					.then((res) => {
						console.log(res);
						this.newLogSource = "";
						this.newLogName = "";
						this.newLogNote = "";
						this.newLogAmount = "";
						this.newLogDate = "";
						this.newLogBudget = "-99";
						this.getMyExpenseLogs();
						this.getMyExpenseSources();
					});

				//wait a bit, or it won't be updated properly
			} else {
				//TODO
				console.log("Error");
			}
		},
		addNewSource() {
			if (this.newSourceName.length > 0) {
				Vue.axios
					.post("/api/v1/source/addSource", {
						sourceName: this.newSourceName,
						userId: this.currentUserId,
						sourceType: "EXPENSE",
					})
					.then((res) => {
						console.log(res);

						this.newSourceName = "";

						//Fetch updated sources
						this.getMyExpenseSources();
					});
			}
		},
		activateSourceManager() {
			this.sourceManagerVisible = true;
		},
		hideSourceManagerPopup() {
			this.sourceManagerVisible = false;
		},
		getMyExpenseLogs() {
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
					console.log(res);
					for (const arrIndex in res.data) {
						let logObj = {
							logName: res.data[arrIndex].name,
							logAmount: res.data[arrIndex].amount,
							logDate: res.data[arrIndex].date,
							logNote: res.data[arrIndex].note,
							sourceType: res.data[arrIndex].source.sourceName,
							sourceId: res.data[arrIndex].source.sourceId,
							budgetId: res.data[arrIndex].budget,
							logId: res.data[arrIndex].logId,
						};
						retrievedLogs.push(logObj);
					}
					this.myExpenseLogs = retrievedLogs;
					// console.log(this.myExpenseLogs);

					// Fill in Today and Monthly array
					var today = new Date();
					var thisYear = String(today.getFullYear());
					var thisMonth = String(today.getMonth() + 1).padStart(
						2,
						"0"
					);
					var thisDate = String(today.getDate()).padStart(2, "0");

					let filteredLog = [];
					for (const index in this.myExpenseLogs) {
						const currentLogDate =
							this.myExpenseLogs[index].logDate.split("-");
						if (
							currentLogDate[0] === thisYear &&
							currentLogDate[1] === thisMonth &&
							currentLogDate[2] === thisDate
						) {
							filteredLog.push(this.myExpenseLogs[index]);
						}
					}
					this.myExpenseLogsToday = filteredLog;

					filteredLog = [];
					for (const index in this.myExpenseLogs) {
						const currentLogDate =
							this.myExpenseLogs[index].logDate.split("-");
						if (
							currentLogDate[0] === thisYear &&
							currentLogDate[1] === thisMonth
						) {
							filteredLog.push(this.myExpenseLogs[index]);
						}
					}
					this.myExpenseLogsThisMonth = filteredLog;

					if (this.onStartup === true) {
						this.logToDisplay = this.myExpenseLogsToday;
						this.onStartup = false;
					}

					if (this.logFilter === "This Month") {
						this.logToDisplay = this.myExpenseLogsThisMonth;
					} else if (this.logFilter === "Today") {
						this.logToDisplay = this.myExpenseLogsToday;
					} else {
						this.logToDisplay = this.myExpenseLogs;
					}
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
					console.log(this.myExpenseSources);
				});
		},
		deleteLog(emittedLogKey) {
			// console.log(emittedLogKey);

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
					this.getMyExpenseLogs();
					this.getMyExpenseSources();
				});
		},
		deleteSource(emittedSourceKey) {
			// console.log(emittedSourceKey);

			const params = new URLSearchParams({ id: emittedSourceKey });
			Vue.axios
				.post(
					"/api/v1/source/deletesource",
					{},
					{
						params,
					}
				)
				.then((res) => {
					console.log(res);
					this.getMyExpenseSources();
				});

			//Fetch updated sources
		},
	},
};
</script>

<style scoped>
.app {
	padding: 30px;
}

.expenseSourceDialog {
	font-family: Helvetica;
	font-size: 18px;
	/* background: white; */
	background-color: whitesmoke;
	color: black;
	border-radius: 15px;
	border: none;
	z-index: 10;
	width: 80%;
	text-align: center;
	overflow: auto;
}

.expenseSourceModal {
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

.sourceComponentContainer {
	overflow: auto;
}

.normalButton {
	border: none;
	font-family: Helvetica;
	float: center;
	background-color: gray;
	border-radius: 5px;
	padding: 5px 20px;
	text-align: center;
	text-decoration: none;
	display: inline-block;
	font-size: 16px;
	transition-duration: 0.2s;
	color: white;
}

.normalButton:hover {
	background-color: lightgray;
	color: white;
}

.gap {
	grid-gap: 1em;
}

.col-container {
	display: grid;
	grid-template-columns: repeat(2, 1fr);
	grid-gap: 1rem;
}

.row-container {
	display: grid;
}

.repeat-1 {
	grid-template-rows: repeat(1, 1fr);
}

.repeat-2 {
	grid-template-rows: repeat(1, 1fr);
}

.item {
	/* background-color: #42b983; */
	/* height: 193px; */
	font-size: 1.5rem;
	font-weight: normal;
	border-radius: 15px;
	padding: 30px;
	border: solid 1px;
}

.item_smaller {
	height: 150px !important;
}

.item_bigger {
	height: 360px !important;
}

.item_long {
	height: 525px !important;
}

h3 {
	margin: 40px 0 0;
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

::-webkit-calendar-picker-indicator {
	background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" width="16" height="15" viewBox="0 0 24 24"><path fill="%23bbbbbb" d="M20 3h-1V1h-2v2H7V1H5v2H4c-1.1 0-2 .9-2 2v16c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm0 18H4V8h16v13z"/></svg>');
}
</style>
