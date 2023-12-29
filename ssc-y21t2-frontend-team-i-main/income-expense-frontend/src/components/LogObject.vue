<template>
	<div class="job">
		<div class="jobText">
			<hr />
			<p>
				<strong>{{ logName }} ({{ logDate }})</strong> ฿{{ logAmount }}
			</p>
			<!-- Buttons for delete and view note -->
			<p>Type: {{ sourceType }}</p>
			<a
				class="btn btn-danger"
				@click="activateDeleteDialog"
				aria-label="Delete"
				title="Delete"
				style="color: red"
			>
				<i class="fa fa-trash-o mr-3" aria-hidden="true"></i>
			</a>
			<a
				class="btn btn-default"
				@click="activateNoteDialog"
				aria-label="Info"
				title="Note"
			>
				<i class="fa fa-info-circle" aria-hidden="true"></i>
			</a>

			<!-- delete popup -->
			<div class="deleteModal" v-if="deleteVisible">
				<div class="deleteDialog" style="color: black">
					Are you sure you want to delete this log?
					<br />
					<button class="deleteButton" @click="sendDeleteLog">
						Delete
					</button>
					<br />
					<button class="dismissButton" @click="hideDeletePopup">
						Dismiss
					</button>
				</div>
			</div>

			<!-- Note popup -->
			<div class="deleteModal" v-if="noteVisible">
				<div class="deleteDialog" style="color: black">
					<p>{{ logNote }}</p>
					<br />
					<button class="dismissButton" @click="hideNotePopup">
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
		"logName",
		"logAmount",
		"logDate",
		"logNote",
		"sourceType",
		"sourceId",
		"budgetId",
		"logId",
	],
	data() {
		return {
			deleteVisible: false,
			noteVisible: false,
		};
	},
	methods: {
		sendDeleteLog() {
			this.$emit("delete-log", this.logId);
			this.hideDeletePopup();
		},
		activateDeleteDialog() {
			this.deleteVisible = true;
		},
		hideDeletePopup() {
			this.deleteVisible = false;
		},
		activateNoteDialog() {
			this.noteVisible = true;
		},
		hideNotePopup() {
			this.noteVisible = false;
		},
	},
};
</script>

<style scoped>
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

.noteButton {
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

.noteButton:hover {
	background-color: lightgray;
	color: white;
}
</style>
