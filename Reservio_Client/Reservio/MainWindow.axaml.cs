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
            this.Content = new Datenerhebung(); 
        }
    }
}