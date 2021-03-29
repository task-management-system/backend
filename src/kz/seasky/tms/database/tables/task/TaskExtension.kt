package kz.seasky.tms.database.tables.task

//fun ResultRow.toTask() = TaskEntity(
//    taskId = this[TaskTable.id],
//    title = this[TaskTable.title],
//    description = this[TaskTable.description],
//    dueDate = this[TaskTable.dueDate].millis,
//    creatorId = this[TaskTable.creatorId]
//)
//
//fun ResultRow.toTaskWithCreator() = TaskWithCreator(
//    taskId = get(TaskTable.id),
//    title = get(TaskTable.title),
//    description = get(TaskTable.description),
//    dueDate = get(TaskTable.dueDate).millis,
//    creator = toUser()
//)
//
//fun ResultRow.toTaskWithCreatorAndDetailId() = TaskWithCreatorAndDetailId(
//    taskId = get(TaskTable.id),
//    detailId = get(DetailTable.id),
//    title = get(TaskTable.title),
//    description = get(TaskTable.description),
//    dueDate = get(TaskTable.dueDate).millis,
//    creator = toUser()
//)

//fun InsertStatement<Number>.toTask(task: TaskCreate) {
//    set(TaskTable.title, task.title)
//    set(TaskTable.description, task.description)
//    set(TaskTable.dueDate, DateTime(task.dueDate))
//    set(TaskTable.creatorId, task.creatorId!!)
//}