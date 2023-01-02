package br.com.tudolinux.a7minutesworkout

object Constants {
    fun defaultExerciseList(): ArrayList<ExerciseModel>{
        val exerciseList = ArrayList<ExerciseModel>()
        val jumpingJacks = ExerciseModel(
            1,
            "Jumping Jacks",
            R.drawable.ic_jumping_jacks
        )
        exerciseList.add(jumpingJacks)

        val abdominalCrunch = ExerciseModel(
            1,
            "Abdominal Crunch",
            R.drawable.ic_abdominal_crunch
        )
        exerciseList.add(abdominalCrunch)

        val highKneesRunning = ExerciseModel(
            2,
            "High Knees Running in Place",
            R.drawable.ic_high_knees_running_in_place
        )
        exerciseList.add(highKneesRunning)

        val lunge = ExerciseModel(
            3,
            "Lunge",
            R.drawable.ic_lunge
        )
        exerciseList.add(lunge)

        val plank = ExerciseModel(
            4,
            "Plank",
            R.drawable.ic_plank
        )
        exerciseList.add(plank)

        val pushUp = ExerciseModel(
            5,
            "Push Up",
            R.drawable.ic_push_up
        )
        exerciseList.add(pushUp)

        val pushUpAndRotation = ExerciseModel(
            6,
            "Push Up And Rotation",
            R.drawable.ic_push_up_and_rotation
        )
        exerciseList.add(pushUpAndRotation)

        val sidePlank = ExerciseModel(
            7,
            "Side Plank",
            R.drawable.ic_side_plank
        )
        exerciseList.add(sidePlank)

        val squat = ExerciseModel(
            8,
            "Squat",
            R.drawable.ic_squat
        )
        exerciseList.add(squat)

        val stepUpOntoChair = ExerciseModel(
            9,
            "Step Up Onto Chair",
            R.drawable.ic_step_up_onto_chair
        )
        exerciseList.add(stepUpOntoChair)

        val ticepsDipOnChair = ExerciseModel(
            10,
            "Triceps Dip on Chair",
            R.drawable.ic_triceps_dip_on_chair
        )
        exerciseList.add(ticepsDipOnChair)

        val wallSit = ExerciseModel(
            11,
            "Wall Sit",
            R.drawable.ic_wall_sit
        )
        exerciseList.add(wallSit)


        return exerciseList
    }
}