package pinacolada.cards.fool.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.Prefs;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.stats.CharStat;
import eatyourbeets.utilities.FieldInfo;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.fool.FoolCard;
import pinacolada.relics.PCLEnchantableRelic;
import pinacolada.relics.fool.PolychromePaintbrush;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class GiftBox extends FoolCard
{
    public static final PCLCardData DATA = Register(GiftBox.class)
            .SetSkill(0, CardRarity.SPECIAL, PCLCardTarget.None)
            .SetColorless();
    private static final FieldInfo<Prefs> _prefs = PCLJUtils.GetField("pref", CharStat.class);

    public GiftBox()
    {
        super(DATA);

        Initialize(0, 0, 5, 12);
        SetUpgrade(0, 0, 2, 4);
        SetAffinity_Star(1, 0, 0);

        SetPurge(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        //TODO: Make this into an actual card and not something for testing
        for (PCLAffinity af : PCLAffinity.Extended()) {
            PCLActions.Bottom.AddAffinity(af, 77).ShowEffect(false);
            PCLActions.Bottom.StackAffinityPower(af, 10).ShowEffect(false);
        }

        PCLActions.Bottom.GainEnergy(99);

        PCLEnchantableRelic enchantable = PCLGameUtilities.GetRelic(PCLEnchantableRelic.class);
        if (enchantable != null)
        {
            enchantable.AddCounter(2);
        }
        PolychromePaintbrush pb = new PolychromePaintbrush();
        pb.AddCounter(5);
        PCLGameUtilities.ObtainRelic(player.hb.cX, player.hb.cY, pb);
    }
}