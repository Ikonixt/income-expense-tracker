<template>
	<v-container>
		<v-row>
			<v-col cols="12" md="12">
				<h1>Retirement Planner</h1>
			</v-col>
			<v-col cols="12" md="8">
				<template>
					<div class="section" id="dashboard">
						<v-btn
							id="lastDay"
							small
							depressed
							color="transparent"
							class="mr-4"
						>
							Last Day
						</v-btn>
						<v-btn
							id="lastWeek"
							small
							depressed
							color="transparent"
							class="mr-4"
						>
							Last Week
						</v-btn>
						<v-btn
							id="lastMonth"
							small
							depressed
							color="transparent"
							class="mr-4"
						>
							Last Month
						</v-btn>
						<div id="chartContainer">
							<GChart
								id="chart"
								type="LineChart"
								:data="retirementChartData"
								@ready="onChartReady"
							/>
							<div id="control"></div>
						</div>
					</div>
				</template>
			</v-col>
			<v-col cols="12" md="4">
				<div class="section" style="padding: 0px">
					<template fill-height>
						<v-card
							class="d-flex flex-coloumn"
							color="transparent"
							flat
						>
							<v-container fill-height fluid>
								<v-row
									class="mx-2 my-1"
									align="center"
									justify="center"
								>
									<v-col cols="12" md="12">
										<h2>Retirement Info</h2>
									</v-col>
									<v-col cols="12" md="12">
										<v-divider></v-divider>
									</v-col>
									<v-col cols="12" md="12">
										<p id="retirementInfo">
											Goal: ฿{{
												this.formatNumber(
													this.grandTotal
												)
											}}
										</p>
										<p id="retirementInfo">
											Monthly Target: ฿{{
												this.formatNumber(
													this.monthlyTarget
												)
											}}
										</p>
										<p id="retirementInfo">
											Retirement Date:
											{{ this.formatDate(this.date) }}
										</p>
									</v-col>
									<v-col cols="12" md="12">
										<v-btn
											color="warning"
											@click="
												setRetirementDate();
												overlay = !overlay;
											"
										>
											<v-icon left> mdi-pencil </v-icon>
											Edit
										</v-btn>
									</v-col>
									<v-overlay
										:absolute="absolute"
										:opacity="opacity"
										:value="overlay"
									>
										<v-menu
											ref="datePickerMenu"
											v-model="datePickerMenu"
											:close-on-content-click="false"
											transition="scale-transition"
											offset-y
											max-width="290px"
											min-width="auto"
										>
											<template
												v-slot:activator="{ on, attrs }"
											>
												<v-text-field
													class="my-3"
													v-model="dateFormatted"
													label="Retirement Date"
													hint="MM/DD/YYYY format"
													persistent-hint
													v-bind="attrs"
													@blur="
														date =
															parseDate(
																dateFormatted
															)
													"
													v-on="on"
													:rules="retirementDateRules"
												></v-text-field>
											</template>
											<v-date-picker
												v-model="date"
												color="primary"
												no-title
												@input="datePickerMenu = false"
											></v-date-picker>
										</v-menu>
										<v-form
											class="my-3"
											ref="form"
											v-model="valid"
											lazy-validation
										>
											<v-text-field
												label="Grand Total"
												v-model="grandTotal"
												:value="grandTotal"
												:rules="grandTotalRules"
												required
											></v-text-field>
											<v-btn
												color="primary"
												@click="
													updateUserRetirementPlanner
												"
												class="mr-4"
											>
												Ok
											</v-btn>
											<v-btn
												color="error"
												@click="
													setRetirementDate();
													setGrandTotalProgress();
													setMonthlyTargetProgress();
													overlay = false;
												"
											>
												Cancel
											</v-btn>
										</v-form>
									</v-overlay>
								</v-row>
							</v-container>
						</v-card>
					</template>
				</div>
			</v-col>
			<v-col cols="12" md="6">
				<div class="section">
					<v-row>
						<v-col cols="12" md="12" align="start">
							<h2>Grand Total Progress</h2>
						</v-col>
						<v-col cols="12" md="12">
							<v-tooltip bottom>
								<template v-slot:activator="{ on, attrs }">
									<v-progress-linear
										class="rounded-lg"
										color="green"
										height="25"
										:value="grandTotalProgress"
										v-bind="attrs"
										v-on="on"
									></v-progress-linear>
								</template>
								<span>
									{{
										this.formatNumber(
											(this.grandTotalProgress *
												this.grandTotal) /
												100
										) +
										"/" +
										this.formatNumber(this.grandTotal)
									}}
								</span>
							</v-tooltip>
						</v-col>
					</v-row>
				</div>
			</v-col>
			<v-col cols="12" md="6">
				<div class="section">
					<v-row>
						<v-col cols="12" md="12" align="start">
							<h2>Monthly Target Progress</h2>
						</v-col>
						<v-col cols="12" md="12">
							<v-tooltip bottom>
								<template v-slot:activator="{ on, attrs }">
									<v-progress-linear
										class="rounded-lg"
										color="blue"
										height="25"
										:value="monthlyTargetProgress"
										v-bind="attrs"
										v-on="on"
									></v-progress-linear>
								</template>
								<!-- <p>{{ this.monthlyTarget }}</p> -->
								<span>
									{{
										this.formatNumber(
											(this.monthlyTargetProgress *
												this.monthlyTarget) /
												100
										) +
										"/" +
										this.formatNumber(this.monthlyTarget)
									}}
								</span>
							</v-tooltip>
						</v-col>
					</v-row>
				</div>
			</v-col>
		</v-row>
	</v-container>
</template>

<script>
import Vue from "vue";

export default {
	data(vm) {
		return {
			currentUserId: this.$store.state.userId,
			Logs: [],
			// for the retirement chart
			chart: null,
			google: null,
			retirementChartData: [],
			retirementChartOptions: {
				legend: { position: "none" },
				chartArea: {
					width: "75%",
				},
				height: 200,
				backgroundColor: "transparent",
				hAxis: {
					textStyle: {
						color: this.$vuetify.theme.themes.dark.success,
					},
					gridlines: {
						color: "transparent",
					},
				},
				vAxis: {
					textStyle: {
						color: this.$vuetify.theme.themes.dark.success,
					},
					gridlines: {
						color: "transparent",
					},
				},
			},
			grandTotal: 0,
			monthlyTarget: 0,
			grandTotalProgress: 0,
			monthlyTargetProgress: 0,
			// for the date picker
			date: new Date(Date.now() - new Date().getTimezoneOffset() * 60000)
				.toISOString()
				.substr(0, 10),
			dateFormatted: vm.formatDate(
				new Date(Date.now() - new Date().getTimezoneOffset() * 60000)
					.toISOString()
					.substr(0, 10)
			),
			datePickerMenu: false,
			retirementDateRules: [
				(input) => !!input || "Retirement Date is required",
				(input) =>
					new Date(input) >= new Date() ||
					"Retirement Date cannot be in the past",
			],
			// for the grand total form
			valid: true,
			grandTotalRules: [(input) => !!input || "Grand Total is required"],
			// for the overlay
			absolute: true,
			opacity: 1,
			overlay: false,
		};
	},
	created() {
		this.setRetirementDate();
		this.setLogs();
		this.setRetirementChartData();
		this.setGrandTotalProgress();
		this.setMonthlyTargetProgress();
	},
	mounted() {
		this.setRetirementDate();
		this.setLogs();
		this.setRetirementChartData();
		this.setGrandTotalProgress();
		this.setMonthlyTargetProgress();
	},
	computed: {
		computedDateFormatted() {
			return this.formatDate(this.date);
		},
	},
	watch: {
		date() {
			this.dateFormatted = this.formatDate(this.date);
		},
	},
	methods: {
		setLogs() {
			const params = new URLSearchParams([["id", this.currentUserId]]);
			let retrievedLogs = [];
			Vue.axios
				.get("/api/v1/log/getuserlog", { params })
				.then((response) => {
					for (const arrIndex in response.data) {
						let logList = response.data;
						let logObj = {
							logName: logList[arrIndex].name,
							logAmount: logList[arrIndex].amount,
							logDate: logList[arrIndex].date,
							logType: logList[arrIndex].type,
						};
						retrievedLogs.push(logObj);
					}
				});
			this.Logs = retrievedLogs;
		},
		setRetirementChartData() {
			const params = new URLSearchParams([["id", this.currentUserId]]);
			let retirementChartData = [];
			Vue.axios
				.get("/api/v1/retirement/getuserretirement", { params })
				.then((response) => {
					this.monthlyTarget = response.data.monthlyTarget;
					this.Logs.sort((a, b) => (a.logDate > b.logDate ? 1 : -1));
					let retirementChartDataDict = {};
					for (const arrIndex in this.Logs) {
						let log = this.Logs[arrIndex];
						let date = new Date(log.logDate);
						let current =
							date in retirementChartDataDict
								? retirementChartDataDict[date]
								: 0;
						retirementChartDataDict[date] =
							log.logType == "INCOME"
								? current + log.logAmount
								: current - log.logAmount;
					}
					let startDate =
						this.Logs.length > 0
							? new Date(this.Logs[0].logDate)
							: new Date();
					let nthDate = startDate;
					let today = new Date();
					let accumulated = 0;
					while (nthDate <= today) {
						let fromDict =
							nthDate in retirementChartDataDict
								? retirementChartDataDict[nthDate]
								: 0;
						accumulated = fromDict + accumulated;
						let row = [nthDate, accumulated, this.grandTotal];
						retirementChartData.push(row);
						let nextDay = new Date(nthDate);
						nextDay.setDate(nthDate.getDate() + 1);
						nthDate = nextDay;
					}
				});
			retirementChartData.unshift(["Date", "Balance", "Goal"]);
			this.retirementChartData = retirementChartData;
		},
		onChartReady(chart, google) {
			this.chart = chart;
			this.google = google;
			let retirementChartData = this.retirementChartData;
			let retirementChartOptions = this.retirementChartOptions;
			let themeSuccess = this.$vuetify.theme.themes.dark.success;
			function drawChart() {
				let data = new google.visualization.DataTable();
				data.addColumn("date", "Date");
				data.addColumn("number", "Balance");
				data.addColumn("number", "Goal");
				setTimeout(() => {
					for (const arrIndex in retirementChartData) {
						if (arrIndex > 0) {
							data.addRow(retirementChartData[arrIndex]);
						}
					}
					var chart = new google.visualization.ChartWrapper({
						chartType: "LineChart",
						containerId: "chart",
						options: retirementChartOptions,
					});
					var control = new google.visualization.ControlWrapper({
						controlType: "ChartRangeFilter",
						containerId: "control",
						options: {
							filterColumnIndex: 0,
							ui: {
								chartOptions: {
									height: 50,
									chartArea: {
										width: "75%",
									},
									backgroundColor: "transparent",
									hAxis: {
										textStyle: {
											color: themeSuccess,
										},
									},
									vAxis: {
										textStyle: {
											color: themeSuccess,
										},
									},
								},
							},
						},
					});
					var dashboard = new google.visualization.Dashboard(
						document.querySelector("#dashboard")
					);
					dashboard.bind([control], [chart]);
					dashboard.draw(data);
					function zoomLastDay() {
						var range = data.getColumnRange(0);
						control.setState({
							range: {
								start: new Date(
									range.max.getFullYear(),
									range.max.getMonth(),
									range.max.getDate() - 1
								),
								end: range.max,
							},
						});
						control.draw();
					}
					function zoomLastWeek() {
						var range = data.getColumnRange(0);
						control.setState({
							range: {
								start: new Date(
									range.max.getFullYear(),
									range.max.getMonth(),
									range.max.getDate() - 7
								),
								end: range.max,
							},
						});
						control.draw();
					}
					function zoomLastMonth() {
						var range = data.getColumnRange(0);
						control.setState({
							range: {
								start: new Date(
									range.max.getFullYear(),
									range.max.getMonth() - 1,
									range.max.getDate()
								),
								end: range.max,
							},
						});
						control.draw();
					}
					google.visualization.events.addListener(
						chart,
						"error",
						function (googleError) {
							google.visualization.errors.removeError(
								googleError.id
							);
						}
					);
					var runOnce = google.visualization.events.addListener(
						dashboard,
						"ready",
						function () {
							google.visualization.events.removeListener(runOnce);
							if (document.addEventListener) {
								document
									.querySelector("#lastDay")
									.addEventListener("click", zoomLastDay);
								document
									.querySelector("#lastWeek")
									.addEventListener("click", zoomLastWeek);
								document
									.querySelector("#lastMonth")
									.addEventListener("click", zoomLastMonth);
							} else if (document.attachEvent) {
								document
									.querySelector("#lastDay")
									.attachEvent("onclick", zoomLastDay);
								document
									.querySelector("#lastWeek")
									.attachEvent("onclick", zoomLastWeek);
								document
									.querySelector("#lastMonth")
									.attachEvent("onclick", zoomLastMonth);
							} else {
								document.querySelector("#lastDay").onclick =
									zoomLastDay;
								document.querySelector("#lastWeek").onclick =
									zoomLastWeek;
								document.querySelector("#lastMonth").onclick =
									zoomLastMonth;
							}
						}
					);
				}, 1000);
			}
			google.visualization.events.addListener(
				chart,
				"error",
				function (googleError) {
					google.visualization.errors.removeError(googleError.id);
				}
			);
			google.visualization.events.addListener(
				chart,
				"ready",
				function () {
					drawChart();
				}
			);
			google.load("visualization", "1", {
				packages: ["controls"],
				callback: drawChart,
			});
		},
		setRetirementDate() {
			const params = new URLSearchParams([["id", this.currentUserId]]);
			Vue.axios
				.get("/api/v1/retirement/getuserretirement", { params })
				.then((response) => {
					this.date = response.data.retirementDate;
				});
		},
		setGrandTotalProgress() {
			const params = new URLSearchParams([["id", this.currentUserId]]);
			Vue.axios
				.get("/api/v1/retirement/getuserretirement", { params })
				.then((response) => {
					this.grandTotal = response.data.grandTotal;
					this.grandTotalProgress =
						this.grandTotal == 0
							? 0
							: (response.data.available / this.grandTotal) * 100;
				});
		},
		setMonthlyTargetProgress() {
			const params = new URLSearchParams([["id", this.currentUserId]]);
			Vue.axios
				.get("/api/v1/retirement/getuserretirement", { params })
				.then((response) => {
					this.monthlyTarget = response.data.monthlyTarget;
					let thisMonthIncome = 0;
					for (const arrIndex in this.Logs) {
						let log = this.Logs[arrIndex];
						let date = new Date(log.logDate);
						let now = new Date();
						let inThisMonth =
							date.getMonth() == now.getMonth() &&
							date.getFullYear() == now.getFullYear();
						// thisMonthIncome += inThisMonth ? log.logAmount : 0;
						if (inThisMonth) {
							thisMonthIncome +=
								log.logType == "INCOME"
									? log.logAmount
									: -log.logAmount;
						}
					}
					this.monthlyTargetProgress =
						this.monthlyTarget == 0
							? 0
							: (thisMonthIncome / this.monthlyTarget) * 100;
				});
		},
		updateUserRetirementPlanner() {
			// check if the input is valid according to grand total and retiement date rules
			function checkRetirementDate(retirementDate, retirementDateRules) {
				let isValid = true;
				retirementDateRules.every((rule) => {
					if (rule(retirementDate) !== true) {
						isValid = false;
						return false;
					}
					return true;
				});
				return isValid;
			}
			if (
				this.$refs.form.validate() &&
				checkRetirementDate(this.date, this.retirementDateRules)
			) {
				const params = new URLSearchParams([
					["id", this.currentUserId],
					["grandTotal", this.grandTotal],
					["retirementDate", this.date],
				]);
				Vue.axios
					.get("/api/v1/retirement/updateuserretirementplanner", {
						params,
					})
					.then((response) => {
						if (response.data.success) {
							this.setGrandTotalProgress();
							this.setMonthlyTargetProgress();
							this.setRetirementChartData();
						}
						this.overlay = false;
						this.onChartReady(this.chart, this.google);
					});
			}
		},
		formatDate(date) {
			if (!date) return null;
			const [year, month, day] = date.split("-");
			return `${month}/${day}/${year}`;
		},
		parseDate(date) {
			if (!date) return null;
			const [month, day, year] = date.split("/");
			return `${year}-${month.padStart(2, "0")}-${day.padStart(2, "0")}`;
		},
		formatNumber(number) {
			return number % 1 != 0
				? (Math.round(number * 100) / 100).toFixed(2)
				: number;
		},
	},
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
div.section {
	border: solid 1px;
	border-radius: 15px;
	padding: 15px;
	overflow: hidden;
}

div#chartContainer {
	border-radius: 15px;
	padding-left: 7.5%;
}

p#retirementInfo {
	font-size: 10pt;
	font-weight: bold;
}
</style>
