		
		function fillDest(moeen,tafsili){
			var moeenId = $$('#hesabMoeen_id').val();
			
			if(moeenId == null || moeenId == ''){
				//alert('==null');
				$$('#'+tafsili+'_desc').autocomplete('option','source','<ul></ul>');
				clearAutocomplete(tafsili);
				$$('#hesabKolName').val('');
				renderOne(tafsili,hesabTafsilis,'0');	
				return;
			}
			clearAutocomplete(tafsili);
			tafsiliMap = moeenTafsiliMap[moeenId];
			//alert(inspect(tafsiliMap,10,10));
			kolMap = moeenKolMap[moeenId];
			//alert("unitMap : "+unitMap.length);
			//var filtered = tafsiliMap;
			//alert(inspect(filtered,10,10));
			$$('#'+tafsili+'_desc').autocomplete('option','source',tafsiliMap);
			 $$('#hesabKolName').val(kolMap.label);
			 //$$('#hesabTafsili_desc').autocomplete({source:filtered});
		}
	

	function updateFooter(){
			var bestankarTotal= sumColumnOfGrid('sanadHesabdariGrid','bestankar');//$$('#sanadHesabdariGrid').jqGrid('getCol', 'bestankar', false, 'sum');
			//alert('bestankarTotal : ' +bestankarTotal);
			$$('#sanadHesabdariGrid').jqGrid('footerData', 'set', { bestankar: numberWithCommas(bestankarTotal)});

			var bedehkarTotal= sumColumnOfGrid('sanadHesabdariGrid','bedehkar');//$$('#sanadHesabdariGrid').jqGrid('getCol', 'bedehkar', false, 'sum');
			//alert('bedehkarTotal : ' +bedehkarTotal);
			$$('#sanadHesabdariGrid').jqGrid('footerData', 'set', { bedehkar: numberWithCommas(bedehkarTotal)});
			
			$$('#sanadHesabdariGrid').jqGrid('footerData', 'set', { description: bedehkarTotal-bestankarTotal});
		}


		
		function sumColumnOfGrid(gridID,column){
			var allRowsInGrid = $$("#"+gridID).jqGrid('getGridParam','data');
			//var allRowsInGrid = $$("#"+gridID).jqGrid('getRowData');
			var sumOfColumn=0;
			if (allRowsInGrid.length!=0){
				for (row in  allRowsInGrid){
					var rowColumn=allRowsInGrid[row][column];
					if(rowColumn != undefined){
						rowColumn=rowColumn+'';
						rowColumn = rowColumn.replace(/\,/g,'');
					 	if(rowColumn != '' && isNumeric(rowColumn)){
					 		sumOfColumn+=parseFloat(rowColumn);
						}
					}
					 
				}
											
			}
			return sumOfColumn;
			
			
			
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
		else if(fieldId == 'hesabTafsiliID')
			bindedFieldId = 'hesabTafsili_id';
		else if(fieldId == 'hesabTafsiliName')
			bindedFieldId = 'hesabTafsili_desc';
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
			if(fieldId == 'hesabTafsiliLevels'){
				$$('input.tafsiliInput').each(function( i ) {
					var id_desc = $( this ).prop('id');
					var id = id_desc.substring(0,id_desc.lastIndexOf("_desc"));
					$$('#'+id+"_desc").val('');
					$$('#'+id+"_id").val('');
				});
				continue;
			}
			if(fieldId == 'hesabTafsiliDescs'){
				continue;
			}
			if(fieldId == 'accountingMarkazLevels'){
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
			}			
			var bindedFieldId=getBindedSanadHesabdariFieldId(fieldId);
			$$('#'+bindedFieldId).val(elems[fieldId]);
		}
	}
	
	
	function getSanadHesabdariDataRow(gridId){
		var datarow={};
		var localItems = getItems(gridId);
		for ( var j = 0; j < localItems.length; j++){
			var fieldId = localItems[j];
			
			if(fieldId == 'hesabTafsiliLevels'){
				var hesabTafsiliLevels='';
				$$('input.tafsiliInput').each(function( i ) {
					var id_desc = $( this ).prop('id');
					var id = id_desc.substring(0,id_desc.lastIndexOf("_desc"));
					var tafsiliValue = $$('#'+id+"_id").val();
					hesabTafsiliLevels = hesabTafsiliLevels +","+id+'='+ tafsiliValue;
				});
				datarow['hesabTafsiliLevels']=hesabTafsiliLevels;
				continue;
			}
			if(fieldId == 'hesabTafsiliDescs'){
				var hesabTafsiliDescs='';
				$$('input.tafsiliInput').each(function( i ) {
					var id_desc = $( this ).prop('id');
					var id = id_desc.substring(0,id_desc.lastIndexOf("_desc"));
					var desc = $( this ).val();
					hesabTafsiliDescs = hesabTafsiliDescs +","+id+'='+ desc;
				});
				datarow['hesabTafsiliDescs']=hesabTafsiliDescs;
				continue;
			}
			if(fieldId == 'hesabTafsiliLevelNames'){
				var hesabTafsiliLevelNames='';
				$$('input.tafsiliInput').each(function( i ) {
					var id_desc = $( this ).prop('id');
					var id = id_desc.substring(0,id_desc.lastIndexOf("_desc"));
					var desc = $( this ).val();
					hesabTafsiliLevelNames = hesabTafsiliLevelNames +","+ desc;
				});
				datarow['hesabTafsiliLevelNames']=hesabTafsiliLevelNames;
				continue;
			}

			
			if(fieldId == 'accountingMarkazLevels'){
				var accountingMarkazLevels='';
				$$('input.accountingMarkazInput').each(function( i ) {
					var id_desc = $( this ).prop('id');
					var id = id_desc.substring(0,id_desc.lastIndexOf("_desc"));
					var accountingMarkazValue = $$('#'+id+"_id").val();
					accountingMarkazLevels = accountingMarkazLevels +","+id+'='+ accountingMarkazValue;
				});
				datarow['accountingMarkazLevels']=accountingMarkazLevels;
				continue;
			}
			if(fieldId == 'accountingMarkazDescs'){
				var accountingMarkazDescs='';
				$$('input.accountingMarkazInput').each(function( i ) {
					var id_desc = $( this ).prop('id');
					var id = id_desc.substring(0,id_desc.lastIndexOf("_desc"));
					var desc = $( this ).val();
					accountingMarkazDescs = accountingMarkazDescs +","+id+'='+ desc;
				});
				datarow['accountingMarkazDescs']=accountingMarkazDescs;
				continue;
			}
			if(fieldId == 'accountingMarkazLevelNames'){
				var accountingMarkazLevelNames='';
				$$('input.accountingMarkazInput').each(function( i ) {
					var id_desc = $( this ).prop('id');
					var id = id_desc.substring(0,id_desc.lastIndexOf("_desc"));
					var desc = $( this ).val();
					accountingMarkazLevelNames = accountingMarkazLevelNames +","+ desc;
				});
				datarow['accountingMarkazLevelNames']=accountingMarkazLevelNames;
				continue;
			}
			
			var convertedFieldId = getBindedSanadHesabdariFieldId(fieldId);
			var val = $$('#'+convertedFieldId).val();
			if(fieldId == 'bedehkar' && val == '')
				val =0;
			if(fieldId == 'bestankar' && val == '')
				val =0;
			datarow[fieldId]=val;
		}
		
		return datarow;
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