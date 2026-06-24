import UIKit
import SwiftUI
import ComposeApp
import WidgetKit

struct ComposeView: UIViewControllerRepresentable {
    var initialRoute: String?

    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController(
            initialRoute: initialRoute,
            onUpdateWidgets: {
                WidgetCenter.shared.reloadAllTimelines()
            }
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var initialRoute: String?

    var body: some View {
        ComposeView(initialRoute: initialRoute)
            .ignoresSafeArea(.keyboard)
    }
}



