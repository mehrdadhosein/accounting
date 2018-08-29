		
	

	function updateFooter(){
			var bestankarTotal= sumColumnOfGrid('sanadHesabdariGrid','bestankar');//$$('#sanadHesabdariGrid').jqGrid('getCol', 'bestankar', false, 'sum');
			//alert('bestankarTotal : ' +bestankarTotal);
			$$('#sanadHesabdariGrid').jqGrid('footerData', 'set', { bestankar: numberWithCommas(bestankarTotal)});

			var bedehkarTotal= sumColumnOfGrid('sanadHesabdariGrid','bedehkar');//$$('#sanadHesabdariGrid').jqGrid('getCol', 'bedehkar', false, 'sum');
			//alert('bedehkarTotal : ' +bedehkarTotal);
			$$('#sanadHesabdariGrid').jqGrid('footerData', 'set', { bedehkar: numberWithCommas(bedehkarTotal)});
			
			$$('#sanadHesabdariGrid').jqGrid('footerData', 'set', { description: round(bedehkarTotal-bestankarTotal)});
		}



	function addSanadHesabdariActionButtons(gridId,rowId){
		del = "<input style='' type='image' src='"+contextPath+"/images/remove.png'  value='Delete' onclick=\"deleteRowData  ('"+gridId+"','"+rowId+"');updateFooter();\" />";
		$$(gridId).jqGrid('setRowData', rowId, {
			act :del
		});

		edit = "<input style=''  type='image' src='"+contextPath+"/images/edit.png'   value='Edit'   onclick=\" return editSanadHesabdariRowData('"+gridId+"','"+rowId+"','"+editDialogTitle+"');\" />";
		$$(gridId).jqGrid('setRowData', rowId, {
			editAct :edit
		});
			
		//$$(gridId).jqGrid('setGridParam',{page:1}).trigger("reloadGrid"); 
	}
	


	function addSanadHesabdariActionButtonsInGrid(index){
        var deleteFunction = 'deleteRowData(getSanadHesabdariGridId(),'+index+');updateFooter();';
		del = '<input style="" type="image" src="'+contextPath+'/images/remove.png"  value="Delete" onclick=\"return '+deleteFunction+'\" />';
		var editFunction = 'editSanadHesabdariRowData(getSanadHesabdariGridId(),'+index+',getEditDialogTitle());';
		//alert(editFunction);
		edit = '<input style=""  type="image" src="'+contextPath+'/images/edit.png"   value="Edit"   onclick=\"return '+editFunction+'\" />';
		return del + "','"+edit + "','"; 
	}
	
	function SetupSanadHesabdariJQgridJson(jsonData,gridId,items) {
		setGridId(gridId);
		setItems(gridId,items);
	    var cells = '';
	    var total_page = 1;
	    var current_page = 1;


		for ( var index = 0; index < jsonData.length; index++){
			var x = jsonData[index];
	        cells += "\r\n{id:'" + index + "', cell:['"

	        for ( var j = 0; j < items.length; j++){
	        	var cellValue = x[items[j]];
	        	if(cellValue == null)
	        		cellValue='';
	        	cells += cellValue + "','"
	        }
	        
	        cells += addSanadHesabdariActionButtonsInGrid(index);
	        

	        
	        if (items.length > 0)
	        	cells = cells.substring(0,cells.length-3);
	        cells += "']},"
//	        	if(index<5)
//	        		alert(cells);

	    }
		if (jsonData.length > 0)
			cells = cells.substring(0,cells.length-1);
		
	    var header = "{" +
		"total: '" + total_page + "'," +
		"page: '" + current_page + "'," +
		"records: '" + jsonData.length + "'," +
		"rows : [";
		var footer = "]" +
				"}";
		//alert(header + cells + footer);
		return header + cells + footer;
	}

	function getBindedSanadHesabdariFieldId(fieldId){
		var bindedFieldId=fieldId;
		if(fieldId == 'hesabMoeenID')
			bindedFieldId = 'hesabMoeen_id';
		else if(fieldId == 'hesabMoeenName')
			bindedFieldId = 'hesabMoeen_desc';
		else if(fieldId == 'hesabTafsiliOneID')
			bindedFieldId = 'hesabTafsiliOne_id';
		else if(fieldId == 'hesabTafsiliOneName')
			bindedFieldId = 'hesabTafsiliOne_desc';
		else if(fieldId == 'hesabTafsiliTwoID')
			bindedFieldId = 'hesabTafsiliTwo_id';
		else if(fieldId == 'hesabTafsiliTwoName')
			bindedFieldId = 'hesabTafsiliTwo_desc';
		else if(fieldId == 'accountingMarkazID')
			bindedFieldId = 'accountingMarkaz_id';
		else if(fieldId == 'accountingMarkazName')
			bindedFieldId = 'accountingMarkaz_desc';		
		else if(fieldId == 'markazHazineID')
			bindedFieldId = 'markazHazine_id';
		else if(fieldId == 'markazHazineName')
			bindedFieldId = 'markazHazine_desc';
		else if(fieldId == 'projectID')
			bindedFieldId = 'project_id';
		else if(fieldId == 'projectName')
			bindedFieldId = 'project_desc';
		return bindedFieldId;
	}

	function clearSanadHesabdariDialog(gridId){
		var localItems = getItems(gridId);
		for ( var j = 0; j < localItems.length; j++){
			fieldId = localItems[j];
			if(fieldId == 'hesabTafsiliOneName'){
				debugger;

				$$('#hesabTafsiliOne_desc').val('');
				$$( "#hesabTafsiliOne_desc" ).autocomplete('option', 'source', hesabTafsilisOne);
				//$$('#hesabTafsiliTwo_desc').val('');
				//$$( "#hesabTafsiliTwo_desc" ).autocomplete('option', 'source', hesabTafsilisTwo);
				
				continue;
			}
			if(fieldId == 'hesabTafsiliTwoName'){
				$$('#hesabTafsiliTwo_desc').val('');
				$$( "#hesabTafsiliTwo_desc" ).autocomplete('option', 'source', hesabTafsilisTwo);

				continue;
			}
/*			if(fieldId == 'hesabTafsiliDescs'){
				continue;
			}*/
/*			if(fieldId == 'accountingMarkazLevels'){
				$$('input.accountingMarkazInput').each(function( i ) {
					var id_desc = $( this ).prop('id');
					var id = id_desc.substring(0,id_desc.lastIndexOf("_desc"));
					$$('#'+id+"_desc").val('');
					$$('#'+id+"_id").val('');
				});
				continue;
			}
			if(fieldId == 'accountingMarkazDescs'){
				continue;
			}			*/
			var bindedFieldId=getBindedSanadHesabdariFieldId(fieldId);
			$$('#'+bindedFieldId).val(elems[fieldId]);
		}
	}
	
	

	
	
	function populateSanadHesabdariRecords(recordsInputId,gridId){
		if(!gridId)
			gridId = "#grid";
		else
			gridId="#"+gridId;
		viewWholeGrid(gridId)

		var records='';
		var allRowsInGrid = $$(gridId).jqGrid('getRowData');
		for ( var i = 0; i < allRowsInGrid.length; i++) {
				var rec = allRowsInGrid[i];
				if(rec['id']==undefined)
					return;
			records=records+"{";
			var localItems = getItems(gridId);
			for ( var j = 0; j < localItems.length; j++){
				//alert(rec[localItems[j]]+' '+localItems[j]);
				records=addToRecords(rec[localItems[j]],records,localItems[j]);
			}
			records = records.substring(0, records.length-1);
			records=records+"},";
		}

		if(allRowsInGrid.length>0)
			records = records.substring(0, records.length-1)
		document.getElementById(recordsInputId).value=records;
		return true;
	}
	
	
	function populateRecordsXML(recordsInputId,gridId){
		if(!gridId)
			gridId = "#grid";
		$$(gridId).jqGrid('saveRow',lastRow, false, 'clientArray');
		//viewWholeGrid(gridId)
		var localItems = getItems(gridId);
		var records='';
		var allRowsInGrid = $$(gridId).jqGrid('getRowData');
		var factorItemsXML = "<?xml version='1.0' encoding='UTF-8'?>\n";

		factorItemsXML +="<rows>\n";
		factorItemsXML +="<page>1</page>\n";
		factorItemsXML +="<total>1</total>\n";
		factorItemsXML +="<records>"+allRowsInGrid.length+"</records>\n";
		var index = 1;
		for ( var i = 0; i < allRowsInGrid.length; i++) {
			var rec = allRowsInGrid[i];
			if(rec['id']==undefined)
				return;
			factorItemsXML +="<row id='"+index+"'>";
			
			
			for ( var j = 0; j < localItems.length; j++){
				//alert(rec[localItems[j]]+' '+localItems[j]);
				factorItemsXML +="<cell name='"+localItems[j]+"'>"+rec[localItems[j]]+"</cell>";
			}
			
	        var deleteFunction = 'deleteRowData(getSanadHesabdariGridId(),'+index+');updateFooter();';
			del = '\<input style="" type="image" src="'+contextPath+'/images/remove.png"  value="Delete" onclick=\"return '+deleteFunction+'\" /\>';
			var editFunction = 'editSanadHesabdariRowData(getSanadHesabdariGridId(),'+index+',getEditDialogTitle());';
			edit = '\<input style=""  type="image" src="'+contextPath+'/images/edit.png"   value="Edit"   onclick=\"return '+editFunction+'\" /\>';
			
			
			factorItemsXML +='<cell>\<![CDATA[ '+del+']]\></cell>';
			factorItemsXML +='<cell>\<![CDATA[ '+edit+']]\></cell>';
			
			factorItemsXML +="</row>";
			++index;
		}
		factorItemsXML +="</rows>\n";
		document.getElementById(recordsInputId).value=factorItemsXML;
		return true;
	}

	function populateRecordsXMLV2(recordsInputId,gridId){
		if(!gridId)
			gridId = "#grid";
		//viewWholeGrid(gridId);
		$$(gridId).jqGrid('saveRow',lastRow, false, 'clientArray');
		var localItems = getItems(gridId);
		var records='';
		var allRowsInGrid = $$(gridId).jqGrid('getRowData');
		var factorItemsXML = "<?xml version='1.0' encoding='UTF-8'?>\n";

		factorItemsXML +="<rows>\n";
		factorItemsXML +="<page>1</page>\n";
		factorItemsXML +="<total>1</total>\n";
		factorItemsXML +="<records>"+allRowsInGrid.length+"</records>\n";
		var index = 1;
		for ( var i = 0; i < allRowsInGrid.length; i++) {
			var rec = allRowsInGrid[i];
			if(rec['id']==undefined )
				return ;
			factorItemsXML +="<row id='"+index+"'>";
			
			
			for ( var j = 0; j < localItems.length; j++){
				//alert(rec[localItems[j]]+' '+localItems[j]);
				if(rec[localItems[j]]==undefined || rec[localItems[j]]=='undefined'){
					rec[localItems[j]]="";
				}
				factorItemsXML +="<cell name='"+localItems[j]+"'>"+rec[localItems[j]]+"</cell>";
			}
			

			
	        var deleteFunction = 'deleteRowData(getSanadHesabdariGridId(),'+index+');updateFooter();';
			del = '\<input style="" type="image" src="'+contextPath+'/images/remove.png"  value="Delete" onclick=\"return '+deleteFunction+'\" /\>';
			var editFunction = 'editSanadHesabdariRowData(getSanadHesabdariGridId(),'+index+',getEditDialogTitle());';
			edit = '\<input style=""  type="image" src="'+contextPath+'/images/edit.png"   value="Edit"   onclick=\"return '+editFunction+'\" /\>';
			
			
			factorItemsXML +='<cell>\<![CDATA[ '+del+']]\></cell>';
			factorItemsXML +='<cell>\<![CDATA[ '+edit+']]\></cell>';
			
			factorItemsXML +="</row>";
			++index;
		}
		factorItemsXML +="</rows>\n";
		document.getElementById(recordsInputId).value=factorItemsXML;
		//alert(factorItemsXML);
		return true;
	}
	
	function addRootHesabsToGrid(gridId, kolDesc, hesabMoeenID, hesabMoeenName, hesabTafsiliID, hesabTafsiliName, description){
		globalRowId = getGridGlobalRowId(gridId); 
		globalRowId++;
		$$(gridId).jqGrid('saveRow',lastRow, false, 'clientArray');
		var datarow={};
		datarow['hesabKolName']=kolDesc;
		datarow['hesabMoeenID']=hesabMoeenID;
		datarow['hesabMoeenName']=hesabMoeenName;
		datarow['hesabTafsiliID']=hesabTafsiliID;
		datarow['hesabTafsiliName']=hesabTafsiliName;
		datarow['description']=description;
		datarow['bedehkar']=0;
		datarow['bestankar']=0;
		datarow['id']=globalRowId;
		
	 	
		var su=$$(gridId).jqGrid('addRowData',globalRowId,datarow);
		setGridGlobalRowId(gridId,globalRowId);
		if(su){
			addSanadHesabdariActionButtons(gridId,globalRowId);
			var lastPageNumber = getLastPageNumber(gridId);
			$$(gridId).jqGrid('setGridParam',{page:lastPageNumber}).trigger('reloadGrid'); 
		}else alert('Can not update');

		$$('#sanadHesabdariGrid').jqGrid('setSelection', globalRowId, true);

		clearAutocomplete('rootHesabs');
		$$('#rootHesabs_desc').focus();
		//$$('#hesabMoeen_desc').focus();
	}	
	
	function resize(){
		//alert($$(window).width());
		if($$(window).width()<768){
			
			$$('#sanadHesabdariGrid').jqGrid('hideCol','hesabKolName'); 
			$$('#sanadHesabdariGrid').jqGrid('hideCol','hesabMoeenName'); 
			$$('#sanadHesabdariGrid').jqGrid('hideCol','accountingMarkazName'); 
			$$('#sanadHesabdariGrid').jqGrid('hideCol','hesabTafsiliName'); 
			$$('#sanadHesabdariGrid').setGridWidth($$(window).width()-20);
		}else{
			$$('#sanadHesabdariGrid').jqGrid('showCol','hesabKolName'); 
			$$('#sanadHesabdariGrid').jqGrid('showCol','hesabMoeenName'); 
			$$('#sanadHesabdariGrid').jqGrid('showCol','accountingMarkazName'); 
			$$('#sanadHesabdariGrid').jqGrid('showCol','hesabTafsiliName'); 

			$$('#sanadHesabdariGrid').setGridWidth($$(window).width()-20);
		}
	}

	function duplicateSanadHesabdariRowData(gridId,rowId,title){
		//alert('editSanadHesabdariRowData');
		$$(gridId).jqGrid('saveRow',lastRow, false, 'clientArray');
		//$$('#sanadHesabdariGrid').jqGrid('setSelection', rowId, true);
		var su=$$(gridId).getRowData(rowId);


			
		var localItems = getItems(gridId);
		for ( var j = 0; j < localItems.length; j++){
			updateSanadHesabdariField(localItems[j],su[localItems[j]]);
		}
		
		addSanadHesabdariRowData('#sanadHesabdariGrid'); 

		return false;
	}	

	function editSanadHesabdariRowData(gridId,rowId,title){
		//alert('editSanadHesabdariRowData');
		$$(gridId).jqGrid('saveRow',lastRow, false, 'clientArray');
		//$$('#sanadHesabdariGrid').jqGrid('setSelection', rowId, true);
		var su=$$(gridId).getRowData(rowId);


			
		var localItems = getItems(gridId);
		for ( var j = 0; j < localItems.length; j++){
			updateSanadHesabdariField(localItems[j],su[localItems[j]]);
		}
		

		$$('#sanadHesabdariDialog').dialog({position: { my: "top", at: "top", of: window  },close: function() {$$(this).off('keydown');},title:title, width: '100%', autoOpen: true, modal:true,buttons: { 'بستن': function() { $$(this).dialog('close'); },'بروزرسانی': function() { changeRowData(gridId,rowId);}}});
		$$('#sanadHesabdariDialog').on('keydown', function(e) {
			
			 var code = (e.keyCode ? e.keyCode : e.which);
			 //alert(code);
			 if(code == 113) { //F2 key updates article in sanadGrid
			   //alert('edit : 113');
				var result = changeRowData(gridId,rowId);
				//alert('F2 : '+rowId);
				//$$('#sanadHesabdariGrid').jqGrid('setSelection', rowId, true);
				lastRow=rowId;
				$$('#sanadHesabdariGrid').focus();
				return false;
			 }
			 if(code == 45) { //INS key adds article to sanadGrid
				//alert('new : 45');
				//alert('INS : '+rowId);
				addSanadHesabdariRowData('#sanadHesabdariGrid');
				$$('#sanadHesabdariGrid').jqGrid('setSelection', rowId, true);
				lastRow=rowId;
				$$('#sanadHesabdariGrid').focus();
				return false;
			 }			 
		});		

		$$('#hesabMoeen_desc').focus();		
		return false;
	}

	function updateSanadHesabdariField(fieldId,fieldValue){
		debugger;
		if(fieldId == 'hesabMoeen_desc'){
			fillDest('hesabMoeen','hesabTafsili');
			fillDestMarkazAccountingByMoeen('hesabMoeen','accountingMarkaz');
		}
		
/*		if(fieldId == 'hesabTafsiliLevels'){
			var hesabTafsiliLevels=fieldValue.split(',');
			for(var index =0 ;index<hesabTafsiliLevels.length; index++){
				if(hesabTafsiliLevels[index] == '')
					continue;
				var keyValue = hesabTafsiliLevels[index].split('=');
				var key = keyValue[0];
				var value = keyValue[1];
				$$('#'+key+'_id').val(value);
			}
			return;
		}
		if(fieldId == 'hesabTafsiliDescs'){
			var hesabTafsiliDescs=fieldValue.split(',');
			for(var index =0 ;index<hesabTafsiliDescs.length; index++){
				if(hesabTafsiliDescs[index] == '')
					continue;
				var keyValue = hesabTafsiliDescs[index].split('=');
				var key = keyValue[0];
				var value = keyValue[1];
				$$('#'+key+'_desc').val(value);
			}
			return;
		}*/
		/*if(fieldId == 'accountingMarkazLevels'){
			var accountingMarkazLevels=fieldValue.split(',');
			for(var index =0 ;index<accountingMarkazLevels.length; index++){
				if(accountingMarkazLevels[index] == '')
					continue;
				var keyValue = accountingMarkazLevels[index].split('=');
				var key = keyValue[0];
				var value = keyValue[1];
				$$('#'+key+'_id').val(value);
			}
			return;
		}
		if(fieldId == 'accountingMarkazDescs'){
			var accountingMarkazDescs=fieldValue.split(',');
			for(var index =0 ;index<accountingMarkazDescs.length; index++){
				if(accountingMarkazDescs[index] == '')
					continue;
				var keyValue = accountingMarkazDescs[index].split('=');
				var key = keyValue[0];
				var value = keyValue[1];
				$$('#'+key+'_desc').val(value);
			}
			return;
		}*/
		var bindedFieldId=getBindedSanadHesabdariFieldId(fieldId);
		$$('#'+bindedFieldId).val(fieldValue);
		fireEvent(document.getElementById(bindedFieldId),'change');
	}

		
	function createSanadHesabdariTable(sanadHesabdariXML){
		var gridItems=new Array('id','hesabKolID','hesabKolName','hesabMoeenID','hesabMoeenName','hesabTafsiliOneID','hesabTafsiliOneName','hesabTafsiliTwoID','hesabTafsiliTwoName','accountingMarkazID','accountingMarkazName','markazHazineID','markazHazineName','projectID','projectName','bedehkar','bestankar','description');
		setItems("#sanadHesabdariGrid",gridItems);
		createSanadHesabdariGrid(sanadHesabdariXML);
		var cnt = $$('#sanadHesabdariGrid').jqGrid('getGridParam', 'records');
		setGridGlobalRowId('#sanadHesabdariGrid',cnt);
		resize();
	}

	function fireRootHesabsOnChange(){
		//debugger;
		var hesabId = $$('#rootHesabs_id').val();
		description = $$('#articleDescription').val();
		if(hesabId.startsWith("Moeen_")){
			var ind = hesabId.indexOf('_');
			var moeenId = hesabId.substring(ind+1); 
			kolMap = moeenKolMap[moeenId];
			kolDesc = kolMap.label;
			moeenDesc = $$('#rootHesabs_desc').val();
			
			addRootHesabsToGrid('#sanadHesabdariGrid', kolDesc, moeenId, moeenDesc, '', '',description);
			
			//alert(hesabMoeenId);
		}else if(hesabId.startsWith("Tafsili_")){
			var ind = hesabId.indexOf('_');
			var hesabTafsiliId = hesabId.substring(ind+1); 
			hesabTafsiliDesc = $$('#rootHesabs_desc').val();
			//alert(hesabTafsiliDesc);
			hesabMoeenList = tafsiliMoeenMap[hesabTafsiliId];
			if(hesabMoeenList.length == 1){
				moeenMap = hesabMoeenList[0];
				var moeenId = moeenMap.value; 
				var moeenDesc = moeenMap.label; 
				kolMap = moeenKolMap[moeenId];
				kolDesc = kolMap.label;
				
				addRootHesabsToGrid('#sanadHesabdariGrid', kolDesc, moeenId, moeenDesc, hesabTafsiliId, hesabTafsiliDesc,description);
			}else{
				showRootNodeMoeenList(hesabTafsiliId, hesabTafsiliDesc, description)
			}
			//alert(hesabTafsiliId);
		}else
			alert('error');
	}

		function showRootNodeMoeenList(hesabTafsiliId, hesabTafsiliDesc, description){
			//debugger;

			var moeenMap = tafsiliMoeenMap[hesabTafsiliId];
			var index =0;
			
			var tbl = $$('#tafsiliMoeenTable');
			tbl.html('');
			var tHead = $$('<thead>');
			var tHeadRow = $$('<tr>');
			tHeadCellKol = $$('<th style="width: 300px;">').append("حساب کل").addClass("ui-state-default");
			tHeadCellMoeen = $$('<th style="width: 300px;">').append("حساب معین").addClass("ui-state-default");
			tHeadRow.append(tHeadCellKol);
			tHeadRow.append(tHeadCellMoeen);
			tHead.append(tHeadRow);
			tbl.append(tHead); 
			
			for(index =0 ; index<moeenMap.length; index++){
				//alert(moeenMap[index].label);
				var moeenId = moeenMap[index].value;
				var moeenDesc = moeenMap[index].label;
				
				kolMap = moeenKolMap[moeenId];
				kolDesc = kolMap.label;
				
				var tRow = $$('<tr>');
				
				kolLabel = moeenKolMap[moeenId].moeenDesc;
				var tStrong1 = $$('<strong>').html(kolDesc);
				var tP1 = $$('<p>').append(tStrong1);
				tCellKol = $$('<td style="width: 300px;padding:5px">').append(tP1).addClass("ui-widget-content");
				
				//addRootHesabsToGrid("#sanadHesabdariGrid", kolDesc, moeenId, moeenDesc, hesabTafsiliId, hesabTafsiliDesc,description);
				var outputLink = '<a onclick="addRootHesabsToGrid('+'\'#sanadHesabdariGrid\',\''+kolDesc+'\',\''+moeenId+'\',\''+moeenDesc+'\',\''+hesabTafsiliId+'\',\''+hesabTafsiliDesc+'\',\''+description+'\');$$(\'#tafsiliMoeenDIV\').dialog(\'close\');" style="height:100px;" class="">'+moeenMap[index].label+'</a>';
				var tStrong2 = $$('<strong>').html(outputLink);
				var tP2 = $$('<p>').append(tStrong2);
				tCellMoeen = $$('<td style="width: 300px;padding:5px">').append(tP2).addClass("ui-widget-content").css({ cursor: "pointer"});
				
				tRow.append(tCellKol);
				tRow.append(tCellMoeen);
				
				//var outputLink = $$('<a onclick="onSelectMoeenOnDialogue('+id+',\''+label+'\','+tafsiliId+',\''+tafsiliDesc+'\')" style="height:100px;" class="">'+moeenMap[index].label+'</a>');
				//$$('#tafsiliMoeenTable').append(outputLink);
				tbl.append(tRow);

			}
			
			$$('#rootHesabsDIV').modal('toggle');
			//$$('#tafsiliMoeenDIV').modal('toggle');
			$$('#tafsiliMoeenDIV').dialog({width: '600px',modal: true});
			//alert("unitMap : "+unitMap.length);
			//var filtered = moeenMap;
			//alert(inspect(filtered,10,10));

		}
		
	/*function fireTafsiliShenavarOnChange(elemId){
		//alert('fireTafsiliShenavarOnChange :' + elemId);
		var ind = elemId.indexOf('_'); 
		//alert(ind);
		var res = elemId.substring(ind-1,ind); 
		var level = parseInt(res);
		var nextLevel = level+1;
		if(nextLevel > maxLevel)
			return;

		var destTafsili = 'hesabTafsili'+nextLevel;
		//alert(destTafsili);
		var srcTafsili = elemId.substring(0,ind);
		fillDestTafsili(srcTafsili, destTafsili);
		fillDestMarkazAccountingByTafsili(srcTafsili, 'accountingMarkaz');
		//alert(inspect(id,10,10));
	}*/
	
	/*function fillDestMarkazAccountingByTafsili(tafsiliSrc,accountingMarkazDest){
		return; //must be corrected in future
		debugger;
		//alert('tafsiliSrc : '+tafsiliSrc);
		//alert('accountingMarkazDest : '+accountingMarkazDest);
		var tafsiliSrcId = $$('#'+tafsiliSrc+'_id').val();
		
		if(tafsiliSrcId == null || tafsiliSrcId=='')
			return;
		clearAutocomplete(accountingMarkazDest);
		accountingMarkazMap = tafsiliAccountingMarkazChildMap[tafsiliSrcId];
		//alert("unitMap : "+unitMap.length);
		var filtered = accountingMarkazMap;
		//alert(inspect(filtered,10,10));
		 $$('#'+accountingMarkazDest+'_desc').autocomplete('option','source',filtered);
	}*/
	
	/*function fireAccountingMarkazShenavarOnChange(elemId){
		//alert(elemId);
		var ind = elemId.indexOf('_'); 
		//alert(ind);
		var res = elemId.substring(ind-1,ind); 
		var level = parseInt(res);
		var nextLevel = level+1;
		if(nextLevel > maxLevel)
			return;
		var destAccountingMarkaz = 'accountingMarkaz'+nextLevel;
		//alert(destAccountingMarkaz);
		var srcAccountingMarkaz = elemId.substring(0,ind);
		fillDestMarkazAccountingByMarkaz(srcAccountingMarkaz, destAccountingMarkaz);
		//alert(inspect(id,10,10));
	}*/

    function isNumeric(n) {
  			return !isNaN(parseFloat(n)) && isFinite(n);
		}           
    
             
		$$('[id="bedehkar"]').digitize();
	   	$$('[id="bestankar"]').digitize();

	   	$$(document).ready(function() {
			var moeenId = $$('#hesabMoeen_id').val();
			
			if(moeenId == null || moeenId == '')
				return;
			fillDest('hesabMoeen','hesabTafsiliOne');
			fillDestMarkazAccountingByMoeen('hesabMoeen','accountingMarkaz');
		});

		

		
		/*function fillDestTafsili(tafsiliSrc,tafsiliDest){
			//alert('tafsiliSrc : '+tafsiliSrc);
			//alert('tafsiliDest : '+tafsiliDest);
			var tafsiliSrcId = $$('#'+tafsiliSrc+'_id').val();
			
			if(tafsiliSrcId == null || tafsiliSrcId=='')
				return;
			clearAutocomplete(tafsiliDest);
			tafsiliMap = tafsiliChildMap[tafsiliSrcId];
			//alert("unitMap : "+unitMap.length);
			var filtered = tafsiliMap;
			//alert(inspect(filtered,10,10));
			 $$('#'+tafsiliDest+'_desc').autocomplete('option','source',filtered);
		}*/
		
		/*function fillDestMarkazAccountingByMarkaz(accountingMarkazSrc,accountingMarkazDest){
			//alert('accountingMarkazSrc : '+accountingMarkazSrc);
			//alert('accountingMarkazDest : '+accountingMarkazDest);
			var accountingMarkazSrcId = $$('#'+accountingMarkazSrc+'_id').val();
			
			if(accountingMarkazSrcId == null || accountingMarkazSrcId=='')
				return;
			clearAutocomplete(accountingMarkazDest);
			accountingMarkazMap = accountingMarkazChildMap[accountingMarkazSrcId];
			//alert("unitMap : "+unitMap.length);
			var filtered = accountingMarkazMap;
			//alert(inspect(filtered,10,10));
			 $$('#'+accountingMarkazDest+'_desc').autocomplete('option','source',filtered);
		}*/
		

		
		function openHesabModal(){
			$$('#hesabLov').modal('show');
		}
		
		function setTafsili(id,desc){
			document.getElementById("hesabTafsiliName").value=desc;
			document.getElementById("hesabTafsiliID").value=id;
			$$('#hesabLov').modal('hide');
		}

		