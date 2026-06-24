import SwiftUI

@main
struct iOSApp: App {
    @State private var initialRoute: String? = nil

    var body: some Scene {
        WindowGroup {
            ContentView(initialRoute: initialRoute)
                .onOpenURL { url in
                    if url.scheme == "malaknyzhka" && url.host == "page" {
                        let page = url.lastPathComponent
                        initialRoute = "book/\(page)"
                    }
                }
        }
    }
}