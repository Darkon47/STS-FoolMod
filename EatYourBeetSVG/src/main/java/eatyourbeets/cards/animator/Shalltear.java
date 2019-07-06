package eatyourbeets.cards.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.powers.RegrowPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import eatyourbeets.actions.common.WaitRealtimeAction;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.actions.common.OnDamageAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.Utilities;

import java.util.ArrayList;

public class Shalltear extends AnimatorCard
{
    public static final String ID = CreateFullID(Shalltear.class.getSimpleName());

    public Shalltear()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL);

        Initialize(11,0, 1);

        AddExtendedDescription();

        this.isMultiDamage = true;

        SetSynergy(Synergies.Overlord);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        ArrayList<AbstractMonster> enemies = PlayerStatistics.GetCurrentEnemies(true);
        for (AbstractMonster m1 : enemies)
        {
            GameActionsHelper.AddToBottom(new VFXAction(new BiteEffect(m1.hb.cX, m1.hb.cY - 40.0F * Settings.scale, Color.SCARLET.cpy()), 0.3F));

            if (HasActiveSynergy() && PlayerStatistics.UseArtifact(m1))
            {
                GameActionsHelper.ApplyPower(p, m1, new StrengthPower(m1, -1), -1);
                GameActionsHelper.ApplyPower(p, p, new StrengthPower(p, 1), 1);
            }
        }

        GameActionsHelper.DamageAllEnemies(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE);
        GameActionsHelper.Callback(new WaitAction(0.1f), this::OnDamage, enemies);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {          
            upgradeDamage(3);
        }
    }

    private void OnDamage(Object state, AbstractGameAction action)
    {
        ArrayList<AbstractMonster> enemies = Utilities.SafeCast(state, ArrayList.class);
        if (enemies != null && action != null)
        {
            for (AbstractMonster monster : enemies)
            {
                if ((monster.isDying || monster.currentHealth <= 0) && !monster.halfDead && !monster.hasPower(MinionPower.POWER_ID))
                {
                    GameActionsHelper.GainEnergy(1);
                    AbstractDungeon.player.heal(3, true);
                    //returnToHand = true;
                }
            }
        }
    }
}