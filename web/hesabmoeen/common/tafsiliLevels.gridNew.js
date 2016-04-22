
	function getBindedFieldId(fieldId){
		var bindedFieldId=fieldId;
		return bindedFieldId;
	}


	function getDataRow(gridId){
		var datarow={};
		var localItems = getItems('#tafsiliLevels');
		for ( var j = 0; j < localItems.length; j++){
			var fieldId = localItems[j];
			var convertedFieldId = fieldId;
			
			if(fieldId == 'hesabTafsiliIds')
				convertedFieldId = "hesabTafsiliIds_id";
			
			datarow[fieldId]=$$('#'+convertedFieldId).val();
		}
		
		return datarow;
	}

	function SetupTafsiliLevelsJQgridJson(jsonData,gridId,items) {
		setGridId(gridId);
		setItems(gridId,items);
	    var cells = "";
	    var total_page = 1;
	    var current_page = 1;


		for ( var index = 1; index <= jsonData.length; index++){
			var x = jsonData[index-1];
	        cells += "\r\n{id:'" + index + "', cell:['"

	        for ( var j = 0; j < items.length; j++){
	        	var cellValue = x[items[j]];
	        	if(cellValue == null)
	        		cellValue='';
	        	cells += cellValue + "','"
	        }
	        
	        cells += addTafsiliLevelsActionButtonsInGrid(index);
			 
	        

	        
	        if (items.length > 0)
	        	cells = cells.substring(0,cells.length-3);
	        cells += "']},"

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
		return header + cells + footer;
	}
	

	function addTafsiliLevelsActionButtonsInGrid(index){
		//alert(getGridId());
		var editFunction = 'editTafsiliLevelsRowData(getGridId(),'+index+',getEditDialogTitle());';
		//alert(editFunction);
		edit = '<input style=""  type="image" src="'+contextPath+'/images/edit.png"   value="Edit"   onclick=\"return '+editFunction+'\" />';
		//alert(edit);
		return edit + "','" ;
	}
	
	 
	function editTafsiliLevelsRowData(gridId,rowId,title){
		if(lastRow!=-1)
			$$('#tafsiliLevels').jqGrid('saveRow',lastRow, false, 'clientArray');
			
		clearMultipleAutocomplete("tafsilies");
							
		var su=$$('#tafsiliLevels').getRowData(rowId);

		var localItems = getItems('#tafsiliLevels');
		for ( var j = 0; j < localItems.length; j++){
			updateField(localItems[j],su[localItems[j]]);
		}
		$$("#dialog").dialog({title:title, width: "60%", autoOpen: true,buttons: { "بستن": function() { $$(this).dialog("close"); },"بروز رسانی": function() { changeTafsiliLevelsRowData('#tafsiliLevels',rowId);$$(this).dialog("close"); }}});
		return false;
	}
	
	
	function changeTafsiliLevelsRowData(gridId,rowId){
		$$('#tafsiliLevels').jqGrid('saveRow',lastRow, false, 'clientArray');
		var datarow = getTafsiliLevelsDataRow('#tafsiliLevels');
		var su=$$('#tafsiliLevels').jqGrid('setRowData',rowId,datarow); 
	}
	
							
	function getTafsiliLevelsDataRow(gridId){
		var datarow={};
		var localItems = getItems(gridId);
		for ( var j = 0; j < localItems.length; j++){
			var fieldId = localItems[j];
			var convertedFieldId = fieldId;
			if(fieldId == 'hesabTafsiliListStr'){
				var result='';
				$$("#tafsilies_div li").each(function( i ) {
					result = result +','+ $$( this ).find('span:first').text();
					//alert( $( this ).find('span:first').text());
					//var id = $( this ).prop('id');
					//var datasizey = $( this ).attr('data-sizey');
				});
				datarow[fieldId]=result;
				continue;
			}
			else if(fieldId == 'hesabTafsiliIds')
				convertedFieldId = "tafsilies_id";
			datarow[fieldId]=$$("#"+convertedFieldId).val();
		}
		return datarow;
	}
	
	function populateTafsiliLevelsRecords(recordsInputId,gridId){
		if(!gridId)
			gridId = "#tafsiliLevels";
		//viewWholeGrid(gridId)

		var records='';
		//var allRowsInGrid = $$(gridId).jqGrid('getRowData');
		var allRowsInGrid = $$(gridId).jqGrid('getGridParam','data');
		for ( var i = 0; i < allRowsInGrid.length; i++) {
				var rec = allRowsInGrid[i];
				if(rec['level']==undefined)
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
			records = records.substring(0, records.length-1);
		document.getElementById(recordsInputId).value=records;
		return true;
	}
	
	function populateTafsiliLevelsRecordsXML(recordsInputId,gridId){
		if(!gridId)
			gridId = "#tafsiliLevels";
		//viewWholeGrid(gridId)
		var localItems = getItems(gridId);
		var records='';
		//var allRowsInGrid = $$(gridId).jqGrid('getRowData');
		var allRowsInGrid = $$(gridId).jqGrid('getGridParam','data');
		var tafsiliLevelsXML = "<?xml version='1.0' encoding='UTF-8'?>\n";

		tafsiliLevelsXML +="<rows>\n";
		tafsiliLevelsXML +="<page>1</page>\n";
		tafsiliLevelsXML +="<total>1</total>\n";
		tafsiliLevelsXML +="<records>"+allRowsInGrid.length+"</records>\n";
		var index = 1;
		for ( var i = 0; i < allRowsInGrid.length; i++) {
			var rec = allRowsInGrid[i];
			if(rec['level']==undefined)
				return;
			tafsiliLevelsXML +="<row id='"+index+"'>";
			
			
			for ( var j = 0; j < localItems.length; j++){
				var value='';
				//alert(rec[localItems[j]]);
				if(rec[localItems[j]]!= undefined && rec[localItems[j]]!= null)
					value=rec[localItems[j]];
				//alert(value);
				tafsiliLevelsXML +="<cell name='"+localItems[j]+"'>"+value+"</cell>";
			}
			
			var editFunction = 'editTafsiliLevelsRowData(getGridId(),'+index+',getEditDialogTitle());';
			edit = '\<input style=""  type="image" src="'+contextPath+'/images/edit.png"   value="Edit"   onclick=\"return '+editFunction+'\" /\>';
			
			
			tafsiliLevelsXML +='<cell>\<![CDATA[ '+edit+']]\></cell>';
			
			tafsiliLevelsXML +="</row>";
			++index;
		}
		tafsiliLevelsXML +="</rows>\n";
		//alert(tafsiliLevelsXML);
		document.getElementById(recordsInputId).value=tafsiliLevelsXML;
		return true;
	}	
