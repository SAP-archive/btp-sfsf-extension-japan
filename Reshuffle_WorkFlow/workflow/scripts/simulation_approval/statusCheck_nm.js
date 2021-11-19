var nextManagers = $.context.nextManagers;
var lastUserTaskAction = $.usertasks.usertask2.last.decision;

if (lastUserTaskAction == "accept") {
    nextManagers.shift();
    $.context.nextManagers = nextManagers;
    $.context.nextProcess_nm = true;

    //配列が空だった場合は、全件が承認されたと判断する
    $.context.nextManagersLength = nextManagers.length;
    // if (nextManagers.length == 0) {
    //     $.context.allNextApproved = true;
    // }

    var allNextCandidates = $.context.allNextCandidates;
    var nmCandidates = $.context.nmCandidates;
    nmCandidates.forEach(function (item, index, array) {
        allNextCandidates.push(item)
    })
    $.context.allNextCandidates = allNextCandidates;


// } else {
//     $.context.nextProcess_nm = false;
}



