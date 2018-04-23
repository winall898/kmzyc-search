
//显示选择赛事的三级连动
function displayThreeSelect(){
    /*
        显示时间下拉框
    */
    var selectTime = '<select name="selectTime" id="selectTime" class="timenp1" onchange="changeTime(this);">';        //时间下拉框数据
    
    var tempDate = new Date();          //临时时间
	var nowDate = new Date();			//现在时间
    for(var i=0;i<7;i++){
        //第i天的时间
        tempDate = new Date(nowDate.getTime()+i*86400000); 
        var dateStr = tempDate.format("yyyy-M-d");
        //组装下拉框中的时间
        if(dateStr==gplayTime){
            selectTime += '<option selected value="'+dateStr+'">'+dateStr+'</option>';
        }else{
            selectTime += '<option value="'+dateStr+'">'+dateStr+'</option>';
        }
    }
	selectTime += '</select>';
    //显示下拉框
    document.getElementById("gameTime").innerHTML = selectTime;

    /*
        显示对阵下拉框
    */

    //对阵下拉框数据
    var selectPlay = '<select name="selectPlay" id="selectPlay" class="leaguenp21">';

    var playArray = selectPlayStr.split("^");
    var gameInitTemp = new Array();
    for(var i=0;i<playArray.length;i++){
        /*
            cellPlay的格式如下： 
            0：gplayId
            1：gameType
            2：gameId
            3：gplayTime （yyy-MM-dd）
            4：hostNameCn
            5：visitNameCn
            6：gliveId
            7：gplayTime（yyyy-MM-dd HH:mm）
        */
        cellPlay = playArray[i].split(",");
        if(cellPlay[3]==gplayTime){     //如果是当前日期的比赛
            if(gameInitTemp.length==0){     //数组为空，增加联赛ID
                gameInitTemp.push(cellPlay[2]);
            }else{
                if(!isExist(cellPlay[2],gameInitTemp)){      //如果数组中存在此联赛
                    gameInitTemp.push(cellPlay[2]);
                }
            }
			
            if(cellPlay[2]==gameId && cellPlay[6]==gliveId){        //并且是当前联赛及当前对阵，则选中
				//对阵value加上主队和客队名字及比赛时间为指数计算所用
                selectPlay += '<option selected value="'+cellPlay[6]+'_'+cellPlay[4]+'_'+cellPlay[5]+'_'+cellPlay[7]+'">'+cellPlay[4]+'VS'+cellPlay[5]+'</option>';
            }else if(cellPlay[2]==gameId){          
                selectPlay += '<option value="'+cellPlay[6]+'_'+cellPlay[4]+"_"+cellPlay[5]+'_'+cellPlay[7]+'">'+cellPlay[4]+'VS'+cellPlay[5]+'</option>';
            }
        }
    }
    selectPlay += '</select>';
    document.getElementById("play").innerHTML = selectPlay;

    /*
        显示联赛下拉框
    */
    var gameArray = selectGameStr.split("^");
    //联赛下拉框数据
    var selectGame = "<select name='selectGame' id='selectGame' class='leaguenp1' onchange='changeGame(this)'>";
    for(var i=0;i<gameArray.length;i++){
        /*
            cellGame的格式如下：
            0：gamType
            1：gameId
            2：gameNameCn
        */
        var cellGame = gameArray[i].split(",");
        for(var j=0;j<gameInitTemp.length;j++){     //循环联赛数组
            if(cellGame[1]==gameInitTemp[j]){       //如果相等，则显示
                if(gameId==cellGame[1]){            //等于当前的联赛ID，则被选中状态
                    selectGame += '<option selected value="'+cellGame[1]+'">'+cellGame[2]+'</option>';
                    continue;
                }else{
                    selectGame += '<option value="'+cellGame[1]+'">'+cellGame[2]+'</option>';
                    continue;
                }
            }
        }
    }
    selectGame += '</select>';
    document.getElementById("game").innerHTML = selectGame;
}


//选择日期
function changeTime(thisObj){

    /*
        选择时间后得出的对阵列表 (选中日期的所有比赛)
    */
    //选中的日期
    var currTime = thisObj.options[thisObj.options.selectedIndex].value;

    var tempGamIdArray = new Array();    //临时联赛ID
    var playData = selectPlayStr.split("^");
    for(var i=0;i<playData.length;i++){
         /*
            playCell的格式如下： 
            0：gplayId
            1：gameType
            2：gameId
            3：gplayTime （yyy-MM-dd）
            4：hostNameCn
            5：visitNameCn
            6：gliveId
            7：gplayTime（yyyy-MM-dd HH:mm）
        */
        var playCell = playData[i].split(",");
        if(currTime==playCell[3]){      //如果是选中时间
            if(tempGamIdArray.length==0){   //放入第一个联赛ID
                tempGamIdArray.push(playCell[2]);
            }else{
                if(!isExist(playCell[2],tempGamIdArray)){   //不存在此gameId在数组中,放入
                    tempGamIdArray.push(playCell[2]);
                }
            }
        }
    }
    /*
        由临时对阵列表获得联赛列表,并显示
    */
    var gameData = selectGameStr.split("^");
    var gameHtml = "<select name='selectGame' id='selectGame' class='leaguenp1' onchange='changeGame(this)'>";
    var flag = true;
    var currGame;       //默认的联赛
    for(var i=0;i<gameData.length;i++){     //循环联赛
         /*
            cellGame的格式如下：
            0：gamType
            1：gameId
            2：gameNameCn
        */
        var gameCell = gameData[i].split(",");
        for(var j=0;j<tempGamIdArray.length;j++){   //循环临时联赛ID
            if(gameCell[1]==tempGamIdArray[j]){     //如果相等
                if(flag){                           //第一个联赛为默认联赛
                    currGame = gameCell[1];
                    flag = false;
                    gameHtml += '<option selected value="'+gameCell[1]+'">'+gameCell[2]+'</option>';
                }else{
                    gameHtml += '<option value="'+gameCell[1]+'">'+gameCell[2]+'</option>';
                }
                continue;
            }
        }
    }
    gameHtml += '</select>';
    document.getElementById("game").innerHTML = gameHtml;

    /*
        由选择的时间及默认的联赛显示对阵列表(currTime和currGame)
    */
    var playHtml = '<select name="selectPlay" id="selectPlay" class="leaguenp21">';
    for(var i=0;i<playData.length;i++){
        /*
            playCell的格式如下： 
            0：gplayId
            1：gameType
            2：gameId
            3：gplayTime （yyy-MM-dd）
            4：hostNameCn
            5：visitNameCn
            6：gliveId
            7：gplayTime（yyyy-MM-dd HH:mm）
        */
        var playCell = playData[i].split(",");
        if(playCell[3]==currTime && playCell[2]==currGame){
			//对阵value加上主队和客队名字及比赛时间为指数计算所用
            playHtml += '<option value="'+playCell[6]+'_'+playCell[4]+'_'+playCell[5]+'_'+playCell[7]+'">'+playCell[4]+'VS'+playCell[5]+'</option>';
        }
    }
    playHtml += '</select>';
    document.getElementById("play").innerHTML = playHtml;
}

//选择联赛
function changeGame(thisObj){
    //选择的联赛ID
    var currGame = thisObj.options[thisObj.options.selectedIndex].value;
    //当前的时间
	var index = document.getElementById('selectTime').options.selectedIndex;
	var currTime = document.getElementById('selectTime').options[index].value;	
    var playHtml = '<select name="selectPlay" id="selectPlay" class="leaguenp21">';
    var playData = selectPlayStr.split("^");
    for(var i=0;i<playData.length;i++){     //循环所有对阵
        /*
            playCell的格式如下： 
            0：gplayId
            1：gameType
            2：gameId
            3：gplayTime （yyy-MM-dd）
            4：hostNameCn
            5：visitNameCn
            6：gliveId
            7：gplayTime（yyyy-MM-dd HH:mm）
        */
        var playCell = playData[i].split(",");
        if(playCell[2]==currGame && playCell[3]==currTime){     //如果比赛时间与联赛ID相同，则显示
			//对阵value加上主队和客队名字及比赛时间为指数计算所用
            playHtml += '<option value="'+playCell[6]+'_'+playCell[4]+'_'+playCell[5]+'_'+playCell[7]+'">'+playCell[4]+'VS'+playCell[5]+'</option>';
        }
    }
    playHtml += '</select>';
    document.getElementById("play").innerHTML = playHtml;
    
}

//判断联赛ID是否存在于此字符串中
function isExist(gameId,array){
    for(var i=0;i<array.length;i++){
        if(array[i]==gameId){
            return true;
        }
    }
    return false;
}

