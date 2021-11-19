var currentManagers = $.context.currentManagers;
var lastUserTaskAction = $.usertasks.usertask1.last.decision;

if (lastUserTaskAction == "accept") {
    currentManagers.shift();
    $.context.currentManagers = currentManagers;

    //配列が空だった場合は、全件が承認されたと判断する
    $.context.allCurrentApproved = false;
    $.context.currentManagersLength = currentManagers.length;

    var allCurrentCandidates = $.context.allCurrentCandidates;
    var cmCandidates = $.context.cmCandidates;
    cmCandidates.forEach(function (item, index, array) {
        allCurrentCandidates.push(item)
    })
    $.context.allCurrentCandidates = allCurrentCandidates;
}



