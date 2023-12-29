<template>
	<div class="app">
		<v-row>
			<v-col cols="12" md="12">
				<h1>Overview</h1>
			</v-col>
			<v-col cols="12" md="12">
				<div class="col-container">
					<div class="row-container gap repeat-1">
						<div
							class="item border section"
							style="overflow-y: auto"
						>
							<strong>Budget</strong>
							<br />
							<v-select
								id="select-budget"
								:items="budgetItems"
								v-model="selectBudget"
								v-on:input="changeBarVal"
								label="Select Budget"
							></v-select>
							<p style="color: red" v-if="overbudget">
								Overbudget
							</p>
							<p>
								Current Amount:
								{{ this.budgetCurrentAmount }} Limit:{{
									this.budgetLimitAmount
								}}
							</p>
							<div>
								<progress
									class="progress"
									id="bar-budget"
									value="0"
									max="100"
								></progress>
							</div>
						</div>
						<div class="item sbody border" style="overflow: auto">
							<div
								v-if="this.recentTransactionList.length > 0"
								class="stext"
							>
								<strong>{{
									this.recentTransactionList[0].date
								}}</strong>
								<div
									v-for="arr in this.recentTransactionList[0]
										.items"
									:key="arr[0] + arr[2]"
								>
									<div>{{ arr[0] }} {{ arr[1] }}</div>
								</div>
							</div>
						</div>
						<div class="item sbody border" style="overflow: auto">
							<div
								v-if="this.recentTransactionList.length > 1"
								class="stext"
							>
								<strong>{{
									this.recentTransactionList[1].date
								}}</strong>
								<div
									v-for="arr in this.recentTransactionList[1]
										.items"
									:key="arr[0] + arr[2]"
								>
									<div>{{ arr[0] }} {{ arr[1] }}</div>
								</div>
							</div>
						</div>
						<div class="item sbody border" style="overflow: auto">
							<div
								v-if="this.recentTransactionList.length > 2"
								class="stext"
							>
								<strong>{{
									this.recentTransactionList[2].date
								}}</strong>
								<div
									v-for="arr in this.recentTransactionList[2]
										.items"
									:key="arr[0] + arr[2]"
								>
									<div>{{ arr[0] }} {{ arr[1] }}</div>
								</div>
							</div>
						</div>
						<div class="item sbody border" style="overflow: auto">
							<div v-if="this.recentTransactionList.length > 3">
								<strong>{{
									this.recentTransactionList[3].date
								}}</strong>
								<div
									v-for="arr in this.recentTransactionList[3]
										.items"
									:key="arr[0] + arr[2]"
								>
									<div>{{ arr[0] }} {{ arr[1] }}</div>
								</div>
							</div>
						</div>
					</div>
					<div class="repeat-2">
						<div class="item border section">
							<strong>Income:</strong> {{ this.totalIncome
							}}<br />
							<strong>Expense:</strong> {{ this.totalExpense
							}}<br />
							<!-- <strong>Retirement:</strong>
							{{ this.totalRetirement }}<br /> -->
							<hr
								style="
									border-top: dashed 1px;
									border-color: black;
								"
							/>
							<strong>Balance:</strong>
							{{ this.totalIncome - this.totalExpense }}
						</div>
						<v-divider class="my-3"></v-divider>
						<!-- <div class="item no-top">Retirement Allocation</div> -->
						<div
							class="item border no-top section"
							style="overflow-y: scroll"
						>
							<strong>Top 5 Income</strong>
							<div
								v-for="array in this.topIncome"
								:key="array[0] + array[2]"
							>
								{{ array[0] }} +{{ array[1] }}
							</div>
						</div>
						<v-divider class="my-3"></v-divider>
						<div
							class="item border no-top section"
							style="overflow-y: scroll"
						>
							<strong>Top 5 Expense</strong>
							<div
								v-for="array in this.topExpense"
								:key="array[0] + array[2]"
							>
								{{ array[0] }} -{{ array[1] }}
							</div>
						</div>
					</div>
				</div>
			</v-col>
		</v-row>
	</div>
</template>

<script>
import "vue-select/dist/vue-select.css";
import Vue from "vue";
export default {
	name: "Overview",
	props: {
		msg: String,
	},
	data() {
		return {
			budgetItems: [],
			budgetItemsObj: {},
			budgetItemsRaw: [],
			recentTransactionList: [],
			topExpense: [],
			topIncome: [],
			totalIncome: 0,
			totalExpense: 0,
			totalRetirement: 0,
			currentUserId: this.$store.state.userId,
			selectBudget: null,
			budgetCurrentAmount: 0,
			budgetLimitAmount: 0,
			overbudget: false,
		};
	},
	watch: {
		selectBudget: function (val) {
			this.overbudget = false;
			console.log(this.budgetItemsRaw);
			for (const index in this.budgetItemsRaw) {
				if (this.budgetItemsRaw[index].name === val) {
					this.budgetCurrentAmount =
						this.budgetItemsRaw[index].currentAmount;
					this.budgetLimitAmount =
						this.budgetItemsRaw[index].totalLimit;

					if (this.budgetLimitAmount < this.budgetCurrentAmount) {
						this.overbudget = true;
					}
				}
			}
		},
	},
	methods: {
		getBudgetList() {
			const params = new URLSearchParams([["id", this.currentUserId]]);
			Vue.axios
				.get("/api/v1/budget/getuserbudget", {
					params,
				})
				.then((res) => {
					for (const arrIndex in res.data) {
						this.budgetItems.push(res.data[arrIndex].name);
						this.budgetItemsObj[res.data[arrIndex].name] =
							res.data[arrIndex].totalLimit;
						this.budgetItemsRaw.push(res.data[arrIndex]);
					}
				});
		},
		getIER() {
			// IER = Income, Expense and Retirement
			var params = new URLSearchParams([["id", this.currentUserId]]);
			Vue.axios
				.get("/api/v1/log/getuserlog", {
					params,
				})
				.then((res) => {
					for (const arrIndex in res.data) {
						if (res.data[arrIndex].type === "INCOME")
							this.totalIncome += res.data[arrIndex].amount;
						if (res.data[arrIndex].type === "EXPENSE")
							this.totalExpense += res.data[arrIndex].amount;
					}
				});
			Vue.axios
				.get("/api/v1/retirement/getuserretirement", {
					params,
				})
				.then((res) => {
					this.totalRetirement +=
						res.data.grandTotal - res.data.available;
				});
			params = new URLSearchParams([
				["id", this.currentUserId],
				["type", "INCOME"],
				["limit", 5],
			]);
			Vue.axios
				.get("/api/v1/log/getuserlog", {
					params,
				})
				.then((res) => {
					let key = 0;
					for (const arrIndex in res.data) {
						this.topIncome.push([
							res.data[arrIndex].name,
							res.data[arrIndex].amount,
							key++,
						]);
					}
				});
			params = new URLSearchParams([
				["id", this.currentUserId],
				["type", "EXPENSE"],
				["limit", 5],
			]);
			Vue.axios
				.get("/api/v1/log/getuserlog", {
					params,
				})
				.then((res) => {
					let key = 0;
					for (const arrIndex in res.data) {
						this.topExpense.push([
							res.data[arrIndex].name,
							res.data[arrIndex].amount,
							key++,
						]);
					}
				});
			params = new URLSearchParams([
				["id", this.currentUserId],
				["sortBy", "date"],
			]);
			Vue.axios
				.get("/api/v1/log/getuserlog", {
					params,
				})
				.then((res) => {
					let key = 0;
					for (const arrIndex in res.data) {
						if (
							this.recentTransactionList.length > 0 &&
							res.data[arrIndex].date ===
								this.recentTransactionList[
									this.recentTransactionList.length - 1
								].date
						) {
							this.recentTransactionList[
								this.recentTransactionList.length - 1
							].items.push([
								res.data[arrIndex].name,
								(res.data[arrIndex].type === "INCOME"
									? "+"
									: "-") + res.data[arrIndex].amount,
								key++,
							]);
							continue;
						}
						if (this.recentTransactionList.length >= 4) break;
						this.recentTransactionList.push({
							date: res.data[arrIndex].date,
							items: [],
						});
						this.recentTransactionList[
							this.recentTransactionList.length - 1
						].items.push([
							res.data[arrIndex].name,
							(res.data[arrIndex].type === "INCOME" ? "+" : "-") +
								res.data[arrIndex].amount,
							key++,
						]);
					}
				});
			console.log(this.recentTransactionList);
			this.topIncome = this.topIncome.reverse();
			this.topExpense = this.topExpense.reverse();
		},
		changeBarVal() {
			document.getElementById("bar-budget").max =
				this.budgetItemsObj[this.selectBudget];
		},
	},
	beforeMount() {
		this.getBudgetList();
		this.getIER();
	},
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.sbody {
	text-decoration: none;
	color: #000;
	background-color: #ffc !important;
	display: block;
	padding: 1em;
	border-radius: 15px;
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
	grid-template-rows: repeat(5, 1fr);
}

.repeat-2 {
	grid-template-rows: repeat(6, 1fr);
}

.item {
	font-size: 1.5rem;
	font-weight: normal;
	/* height: 193px; */
}

.no-top {
	border-top: none;
}

.gap {
	grid-gap: 1em;
}

.header {
	border-style: solid;
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

div.section {
	border: solid 1px;
	border-radius: 15px;
	padding: 15px;
	overflow: hidden;
}
</style>
