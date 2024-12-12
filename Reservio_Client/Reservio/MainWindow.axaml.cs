using Avalonia.Controls;
using Avalonia.Interactivity;

namespace Reservio
{
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();
            this.WindowState = WindowState.Maximized;
        }

        private void OnWeiterButtonClick(object sender, RoutedEventArgs e)
        {
            // Lade die neue Seite in das ContentControl (hier kannst du eine andere Seite laden)
            this.Content = new Reservierungsdaten();  // `SecondPage` muss ein UserControl sein
        }
    }
}