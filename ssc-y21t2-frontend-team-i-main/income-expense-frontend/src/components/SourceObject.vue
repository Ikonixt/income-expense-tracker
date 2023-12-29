<template>
	<div class="job">
		<div class="jobText">
			<hr />
			<div id="sourceInfo">
				<h4 id="nameText" style="display: inline-block">Name:</h4>
				{{ sourceName }}
				<a
					class="btn btn-default"
					@click="activateRenameDialog"
					aria-label="Pencil"
					title="Rename"
				>
					<i class="fa fa-pencil-square-o" aria-hidden="true"></i>
				</a>
				<h4 id="usageText" style="display: inline-block">Usage:</h4>
				{{ sourceUsage }}

				<a
					class="btn btn-danger"
					@click="activateDeleteDialog"
					aria-label="Delete"
					title="Delete"
				>
					<i class="fa fa-trash-o" aria-hidden="true"></i>
				</a>
			</div>
			<div class="modal" v-if="deleteVisible">
				<div class="dialog">
					Are you sure you want to delete this source?
					<br />
					<button class="deleteButton" @click="sendDeleteSource">
						Delete</button
					>&nbsp;
					<button class="normalButton" @click="hideDeletePopup">
						Dismiss
					</button>
				</div>
			</div>

			<div class="modal" v-if="renameVisible">
				<div class="dialog">
					<form @submit.prevent="sendRenameSourceName">
						<p><strong>New Name: </strong></p>
						<input
							class="border"
							id="newSourceName"
							type="text"
							v-model="newSourceName"
							required
						/>
						<br />
						<br />
						<button class="normalButton">Rename</button>&nbsp;
						<button class="normalButton" @click="hideRenamePopup">
							Dismiss
						</button>
					</form>
				</div>
			</div>
			<hr />
		</div>
	</div>
</template>

<script>
export default {
	props: ["sourceId", "sourceName", "sourceUsage"],
	data() {
		return {
			deleteVisible: false,
			newSourceName: "",
			renameVisible: false,
		};
	},
	methods: {
		sendDeleteSource() {
			this.$emit("delete-source", this.sourceId);
			this.hideDeletePopup();
		},
		sendRenameSourceName() {
			this.$emit("source-rename", {
				sourceId: this.sourceId,
				sourceName: this.newSourceName,
			});
			// console.log(this.newSourceName);
			this.newSourceName = "";
			this.hideRenamePopup();
		},
		activateDeleteDialog() {
			// signal parent component
			this.deleteVisible = true;
		},
		hideDeletePopup() {
			this.deleteVisible = false;
		},
		activateRenameDialog() {
			// signal parent component
			this.renameVisible = true;
		},
		hideRenamePopup() {
			this.renameVisible = false;
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
</style>
