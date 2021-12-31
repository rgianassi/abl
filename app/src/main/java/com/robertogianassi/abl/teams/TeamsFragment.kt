package com.robertogianassi.abl.teams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.robertogianassi.abl.R

class TeamsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_teams_grid, container, false)
        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = GridLayoutManager(
                    context,
                    2, // Span Count (columns)
                    GridLayoutManager.VERTICAL,
                    false, // Reverse layout
                )
                adapter = TeamsGridAdapter(UITeam.allTeams)
            }
        }
        return view
    }
}
