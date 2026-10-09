import WidgetKit
import SwiftUI
import ComposeApp

struct ContinueReadingProvider: TimelineProvider {
    private var isUk: Bool {
        Locale.current.language.languageCode?.identifier == "uk"
    }

    func placeholder(in context: Context) -> ContinueReadingEntry {
        ContinueReadingEntry(
            date: Date(),
            pageNumber: 1,
            excerpt: isUk ? "Завантаження..." : "Loading...",
            isPlaceholder: true
        )
    }

    func getSnapshot(in context: Context, completion: @escaping (ContinueReadingEntry) -> ()) {
        WidgetBridge.shared.getWidgetData { data in
            let entry = ContinueReadingEntry(
                date: Date(),
                pageNumber: data != nil ? Int(data!.pageNumber) : 1,
                excerpt: data?.excerpt ?? (isUk ? "Продовжити читання" : "Continue Reading"),
                isPlaceholder: data == nil
            )
            completion(entry)
        }
    }

    func getTimeline(in context: Context, completion: @escaping (Timeline<ContinueReadingEntry>) -> ()) {
        WidgetBridge.shared.getWidgetData { data in
            let entry = ContinueReadingEntry(
                date: Date(),
                pageNumber: data != nil ? Int(data!.pageNumber) : 1,
                excerpt: data?.excerpt ?? (isUk ? "Продовжити читання" : "Continue Reading"),
                isPlaceholder: data == nil
            )
            let timeline = Timeline(entries: [entry], policy: .atEnd)
            completion(timeline)
        }
    }
}

struct ContinueReadingEntry: TimelineEntry {
    let date: Date
    let pageNumber: Int
    let excerpt: String
    let isPlaceholder: Bool
}

struct ContinueReadingWidgetEntryView: View {
    var entry: ContinueReadingProvider.Entry

    private var isUk: Bool {
        Locale.current.language.languageCode?.identifier == "uk"
    }

    private var appName: String {
        isUk ? "Тарас Шевченко ✦" : "Taras Shevchenko ✦"
    }

    private var pageLabel: String {
        isUk ? "Сторінка \(entry.pageNumber)" : "Page \(entry.pageNumber)"
    }

    var body: some View {
        HStack(spacing: 0) {
            // Subtle vertical spine bar along the left edge in ink blue
            Rectangle()
                .fill(Color(hex: "1B365D"))
                .frame(width: 4)

            VStack(alignment: .leading, spacing: 4) {
                Text(appName)
                    .font(.system(size: 13, weight: .bold))
                    .foregroundColor(Color(hex: "1B365D"))

                // Single thin ink-blue rule under header
                Rectangle()
                    .fill(Color(hex: "1B365D"))
                    .frame(height: 1)

                if entry.isPlaceholder {
                    Text(isUk ? "Продовжити читання" : "Continue Reading")
                        .font(.system(size: 15, weight: .bold))
                        .foregroundColor(Color(hex: "2B2D31"))
                    Text(isUk ? "Відкрийте додаток" : "Open the app to start")
                        .font(.system(size: 12))
                        .foregroundColor(Color(hex: "5C5E66"))
                } else {
                    // Page number as hero (largest element)
                    Text(pageLabel)
                        .font(.system(size: 16, weight: .bold))
                        .foregroundColor(Color(hex: "2B2D31"))

                    Text(entry.excerpt)
                        .font(.system(size: 12))
                        .lineLimit(3)
                        .foregroundColor(Color(hex: "2B2D31"))
                }
                Spacer()
            }
            .padding(.all, 10)
        }
        .containerBackground(for: .widget) {
            Color(hex: "F4F5F7")
        }
        .widgetURL(URL(string: "malaknyzhka://page/\(entry.pageNumber)"))
    }
}

struct ContinueReadingWidget: Widget {
    let kind: String = "ContinueReadingWidget"

    private var isUk: Bool {
        Locale.current.language.languageCode?.identifier == "uk"
    }

    var body: some WidgetConfiguration {
        StaticConfiguration(kind: kind, provider: ContinueReadingProvider()) { entry in
            ContinueReadingWidgetEntryView(entry: entry)
        }
        .configurationDisplayName(isUk ? "Продовжити читання" : "Continue Reading")
        .description(isUk ? "Швидко повертайтеся до останньої відкритої сторінки." : "Quickly return to your last opened page.")
        .supportedFamilies([.systemSmall, .systemMedium])
    }
}

extension Color {
    init(hex: String) {
        let scanner = Scanner(string: hex)
        _ = scanner.scanString("#")
        var rgbValue: UInt64 = 0
        scanner.scanHexInt64(&rgbValue)
        let r = Double((rgbValue & 0xFF0000) >> 16) / 255.0
        let g = Double((rgbValue & 0x00FF00) >> 8) / 255.0
        let b = Double(rgbValue & 0x0000FF) / 255.0
        self.init(red: r, green: g, blue: b)
    }
}
