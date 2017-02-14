var closeDialog; 
function huoqudaibanDialog(url,title,width,height) {
 	var userDig = dialog({
				title : title,
				content : '<iframe src=' + url
						+ ' style="border: 0; width: 100%; height: 100%"  frameborder="0"/>',
				width :width,
				height : height
			});
	closeDialog =userDig.showModal();
}
//关闭dialog
function closeHQDBDig(){
	closeDialog.close();
}