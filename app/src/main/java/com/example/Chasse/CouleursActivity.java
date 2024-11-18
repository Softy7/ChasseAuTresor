<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="@drawable/background_main"
        tools:context=".CouleursActivity">

<LinearLayout
        android:id="@+id/linearLayout"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_margin="0dp"
                android:layout_marginTop="20dp"
                android:background="@drawable/btn"
                android:gravity="center"
                android:orientation="horizontal"
                android:paddingHorizontal="10dp"
                android:paddingVertical="10dp"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintHorizontal_bias="0.496"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent"
                app:layout_constraintVertical_bias="0.083" />

<LinearLayout
        android:id="@+id/linearLayout2"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="20dp"
                android:background="@drawable/btn"
                android:gravity="center"
                android:orientation="horizontal"
                android:paddingHorizontal="10dp"
                android:paddingVertical="10dp"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintHorizontal_bias="0.496"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@+id/linearLayout"
                app:layout_constraintVertical_bias="0.078" />

<Button
        android:id="@+id/red"
                android:layout_width="100dp"
                android:layout_height="100dp"
                android:layout_marginStart="16dp"
                android:layout_marginTop="328dp"
                android:backgroundTint="#FF0000"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@+id/linearLayout"
                android:onClick="red"/>

<Button
        android:id="@+id/blue"
                android:layout_width="100dp"
                android:layout_height="100dp"
                android:layout_marginStart="24dp"
                android:layout_marginTop="360dp"
                android:backgroundTint="#00FF00"
                app:layout_constraintStart_toEndOf="@+id/red"
                app:layout_constraintTop_toBottomOf="@+id/linearLayout"
                android:onClick="green"/>

<Button
        android:id="@+id/green"
                android:layout_width="100dp"
                android:layout_height="100dp"
                android:layout_marginStart="40dp"
                android:layout_marginTop="320dp"
                android:backgroundTint="#0000FF"
                app:layout_constraintStart_toEndOf="@+id/blue"
                app:layout_constraintTop_toBottomOf="@+id/linearLayout"
                android:onClick="blue"/>


</androidx.constraintlayout.widget.ConstraintLayout>